package gitp.scrapingbatch.batch.component

import com.fasterxml.jackson.databind.JsonNode
import gitp.entity.Dpt
import gitp.entity.ObjectMappingError
import gitp.scrapingbatch.dto.payload.LecturePayloadDto
import gitp.scrapingbatch.dto.response.LectureResponseDto
import gitp.scrapingbatch.exception.ResolutionException
import gitp.scrapingbatch.repository.DptRepository
import gitp.scrapingbatch.repository.ObjectMappingErrorRepository
import gitp.scrapingbatch.request.YonseiHttpClient
import gitp.scrapingbatch.request.YonseiObjectProducer
import gitp.scrapingbatch.request.YonseiUrlContainer
import gitp.scrapingbatch.service.LectureResponsePersistService
import gitp.scrapingbatch.utils.MyUtils
import gitp.type.Semester
import gitp.yonseiprotohttp.payload.PayloadBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.StepExecution
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
    private val lectureResponsePersistService: LectureResponsePersistService,
    private val objectMappingErrorRepository: ObjectMappingErrorRepository
) : Tasklet, StepExecutionListener {
    private val log: Logger = LoggerFactory.getLogger(LectureRequestAndPersistTasklet::class.java)

    private val client = YonseiHttpClient.of<LectureResponseDto>(
        YonseiUrlContainer.lectureUrl,
        MyUtils.getCommonObjectMapper(),
        mapOf("rmvlcYnNm" to "폐강"),
    ) { jsonNode: JsonNode ->
        jsonNode.path("dsSles251")
    }

    private var cursor: Long = 0L

    private val resolutionExceptionList: MutableList<ResolutionException> = mutableListOf()
    override fun execute(
        contribution: StepContribution, chunkContext: ChunkContext
    ): RepeatStatus? {

        val dpt: Dpt = (dptRepository.findByIdOrNull(++cursor)
            ?: if (cursor == 1L) throw IllegalStateException("can't read any row from dpt table")
            else return RepeatStatus.FINISHED)

        log.info("request and persist lecture of [{}]", dpt.dptName)

        val objectProducer: YonseiObjectProducer<LectureResponseDto> =
            client.retrieveAndGetObjectProducer(
                PayloadBuilder.toPayload(
                    LecturePayloadDto(
                        year,
                        semester,
                        dpt.dptGroup.dptGroupId,
                        dpt.dptId
                    )
                )
            ) as YonseiObjectProducer<LectureResponseDto>

        while (true) {
            val lectureResponseDto: LectureResponseDto
            try {
                lectureResponseDto = objectProducer.pop() ?: break
                lectureResponsePersistService.save(lectureResponseDto)
            } catch (e: ResolutionException) {
                resolutionExceptionList.add(e)
                log.warn("object-mapping exception :${e.message}")
            }
        }

        return RepeatStatus.CONTINUABLE
    }

    override fun afterStep(stepExecution: StepExecution): ExitStatus? {
        log.warn("failed object mapping for (${resolutionExceptionList.size})times")
        resolutionExceptionList
            .map {
                ObjectMappingError(
                    null,
                    it.message!!,
                    it.rawResponseJson!!,
                    stepExecution.jobExecutionId
                )
            }
            .forEach {
                objectMappingErrorRepository.save(it)
            }

        return ExitStatus.COMPLETED
    }
}