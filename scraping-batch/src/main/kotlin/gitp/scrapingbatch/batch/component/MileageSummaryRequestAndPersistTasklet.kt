package gitp.scrapingbatch.batch.component

import com.fasterxml.jackson.databind.JsonNode
import gitp.entity.Lecture
import gitp.entity.ObjectMappingError
import gitp.scrapingbatch.dto.payload.MileageSummaryPayloadDto
import gitp.scrapingbatch.dto.response.LectureIdDto
import gitp.scrapingbatch.dto.response.MileageSummaryDto
import gitp.scrapingbatch.exception.ResolutionException
import gitp.scrapingbatch.repository.LectureRepository
import gitp.scrapingbatch.repository.MileageSummaryRepository
import gitp.scrapingbatch.repository.ObjectMappingErrorRepository
import gitp.scrapingbatch.request.YonseiHttpClient
import gitp.scrapingbatch.request.YonseiObjectProducer
import gitp.scrapingbatch.request.YonseiUrlContainer
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

open class MileageSummaryRequestAndPersistTasklet(
    private val year: Year,
    private val semester: Semester,
    private val lectureRepository: LectureRepository,
    private val mileageSummaryRepository: MileageSummaryRepository,
    private val objectMappingErrorRepository: ObjectMappingErrorRepository,
) : Tasklet, StepExecutionListener {
    private val log: Logger =
        LoggerFactory.getLogger(MileageSummaryRequestAndPersistTasklet::class.java)

    private val client = YonseiHttpClient.of<MileageSummaryDto>(
        YonseiUrlContainer.mileageSummaryUrl,
        MyUtils.getCommonObjectMapper(),
    ) { jsonNode: JsonNode ->
        jsonNode.path("dsSles251")
    }

    private val resolutionExceptionList: MutableList<ResolutionException> = mutableListOf()

    private var cursor: Long = 0L
    override fun execute(
        contribution: StepContribution,
        chunkContext: ChunkContext
    ): RepeatStatus? {
        // 강의들 하나씩 가져오기
        val lecture: Lecture = (lectureRepository.findByIdOrNull(++cursor)
            ?: if (cursor == 1L) throw IllegalStateException("can't read any lecture from table")
            else return RepeatStatus.FINISHED)

        log.info("request and persist mileage summary of [{}]", lecture.name)
        // 강의 아이디 뽑아서 objectProducer 가져오기
        val objectProducer: YonseiObjectProducer<MileageSummaryDto> =
            client.retrieveAndGetObjectProducer(
                PayloadBuilder.toPayload(
                    MileageSummaryPayloadDto(
                        year,
                        semester,
                        LectureIdDto(
                            null,
                            lecture.lectureId.mainId,
                            lecture.lectureId.classDivisionId,
                            lecture.lectureId.subId,
                        )
                    )
                )
            ) as YonseiObjectProducer<MileageSummaryDto>

        // 하나씩 pop 시키면서 저장소에 저장
        while (true) {
            val mileageSummaryDto: MileageSummaryDto
            try {
                mileageSummaryDto = objectProducer.pop() ?: break
                mileageSummaryRepository.save(mileageSummaryDto.toEntity(lecture))
            } catch (e: ResolutionException) {
                resolutionExceptionList.add(e)
                log.warn("object-mapping exception :${e.message}")
            }
        }

        // 예외 발생하면 context에 예외 개수 저장하고
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