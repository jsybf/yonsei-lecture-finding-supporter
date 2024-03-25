package gitp.scrapingbatch.batch.component

import com.fasterxml.jackson.databind.JsonNode
import gitp.entity.Dpt
import gitp.scrapingbatch.dto.payload.LecturePayloadDto
import gitp.scrapingbatch.dto.response.LectureResponseDto
import gitp.scrapingbatch.repository.DptRepository
import gitp.scrapingbatch.request.YonseiHttpClient
import gitp.scrapingbatch.request.YonseiUrlContainer
import gitp.scrapingbatch.service.LectureResponsePersistService
import gitp.scrapingbatch.utils.MyUtils
import gitp.type.Semester
import gitp.yonseiprotohttp.payload.PayloadBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.StepExecutionListener
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.data.repository.findByIdOrNull
import java.time.Year


open class LectureRequestAndPersistTasklet(
    private val year: Year,
    private val semester: Semester,
    private val dptRepository: DptRepository,
    private val lectureResponsePersistService: LectureResponsePersistService
) : Tasklet, StepExecutionListener {
    private val log: Logger = LoggerFactory.getLogger(LectureRequestAndPersistTasklet::class.java)

    private val client = YonseiHttpClient.of<List<LectureResponseDto>>(
        YonseiUrlContainer.lectureUrl,
        MyUtils.getCommonObjectMapper(),
        mapOf("rmvlcYnNm" to "폐강"),
    ) { jsonNode: JsonNode ->
        jsonNode.path("dsSles251")
    }

    private var cursor: Long = 0L

    // dptGroupCount is initialed not in construct-time but by beforeStep method
    // since lateinit doesn't support val,I can't find solution to make it val
    private var dptGroupCount: Long = 0
    override fun execute(
        contribution: StepContribution, chunkContext: ChunkContext
    ): RepeatStatus? {

        val dpt: Dpt = (dptRepository.findByIdOrNull(++cursor)
            ?: if (cursor == 1L) throw IllegalStateException("can't read any row from dpt table")
            else return RepeatStatus.FINISHED)

        log.info("request and persist lecture of [{}]", dpt.dptName)

        val lectureResponseDtoList: List<LectureResponseDto> = client.retrieveAndMap(
            PayloadBuilder.toPayload(
                LecturePayloadDto(
                    year,
                    semester,
                    dpt.dptGroup.dptGroupId,
                    dpt.dptId
                )
            )
        )

        for (responseDto in lectureResponseDtoList) {
            lectureResponsePersistService.save(responseDto)
        }

        return RepeatStatus.CONTINUABLE
    }
}