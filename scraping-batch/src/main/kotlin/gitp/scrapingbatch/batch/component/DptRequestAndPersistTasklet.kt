package gitp.scrapingbatch.batch.component

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import gitp.entity.Dpt
import gitp.entity.DptGroup
import gitp.scrapingbatch.dto.payload.DptPayloadDto
import gitp.scrapingbatch.dto.response.DptResponseDto
import gitp.scrapingbatch.repository.DptGroupRepository
import gitp.scrapingbatch.repository.DptRepository
import gitp.scrapingbatch.request.YonseiHttpClient
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


open class DptRequestAndPersistTasklet(
    private val dptGroupRepository: DptGroupRepository,
    private val dptRepository: DptRepository,
) : Tasklet, StepExecutionListener {
    private val log: Logger = LoggerFactory.getLogger(DptRequestAndPersistTasklet::class.java)
    private val requestUrl: String =
        "https://underwood1.yonsei.ac.kr/sch/sles/SlescsCtr/findSchSlesHandbList.do"

    private val client = YonseiHttpClient.of<List<DptResponseDto>>(
        requestUrl,
        ObjectMapper().registerKotlinModule()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    ) { jsonNode: JsonNode ->
        jsonNode.path("dsFaclyCd")
    }

    private var cursor: Long = 0L

    // dptGroupCount is initialed not in construct-time but by beforeStep method
    // since lateinit doesn't support val,I can't find solution to make it val
    private var dptGroupCount: Long = 0
    override fun beforeStep(stepExecution: StepExecution) {
        dptGroupCount = dptGroupRepository.count()
    }

    override fun afterStep(stepExecution: StepExecution): ExitStatus? {
        log.info("completed dpt groups: ($cursor)/($dptGroupCount)")
        return stepExecution.exitStatus
    }

    override fun execute(
        contribution: StepContribution, chunkContext: ChunkContext
    ): RepeatStatus? {

        if (dptGroupCount == cursor) {
            return RepeatStatus.FINISHED
        }
        val dptGroup: DptGroup = (dptGroupRepository.findByIdOrNull(++cursor)
            ?: throw NoSuchElementException("cant find id with $cursor"))

        log.info("request and persist dpts belong to (${dptGroup.dptGroupName})")

        val payloads = PayloadBuilder.toPayload(
            DptPayloadDto(
                dptGroup.dptGroupId,
                Year.of(2024),
                Semester.FIRST
            )
        )
        // TODO: exception handling if response is empty or response code isn't 200
        val responseDtoList: List<DptResponseDto> = client.retrieveAndMap(payloads)

        // TODO: exception handling if jdbc throw exception
        for (dto in responseDtoList) {
            dptRepository.save(
                Dpt(
                    null,
                    dto.dptName,
                    dto.dptId,
                    dptGroup
                )
            )
        }
        return RepeatStatus.CONTINUABLE
    }
}