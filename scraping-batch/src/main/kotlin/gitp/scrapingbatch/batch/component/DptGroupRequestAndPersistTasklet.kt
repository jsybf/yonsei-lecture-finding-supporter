package gitp.scrapingbatch.batch.component

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import gitp.scrapingbatch.dto.payload.DptGroupPayloadDto
import gitp.scrapingbatch.dto.response.DptGroupResponseDto
import gitp.scrapingbatch.request.YonseiHttpClient
import gitp.type.Semester
import gitp.yonseiprotohttp.payload.PayloadBuilder
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.jdbc.core.JdbcTemplate
import java.time.Year

class DptGroupRequestAndPersistTasklet(
    private val year: Year,
    private val semester: Semester,
    private val jdbcTemplate: JdbcTemplate,
) : Tasklet {
    private val requestUrl: String =
        "https://underwood1.yonsei.ac.kr/sch/sles/SlescsCtr/findSchSlesHandbList.do"

    private val client = YonseiHttpClient.of<List<DptGroupResponseDto>>(
        requestUrl,
        ObjectMapper()
            .registerKotlinModule()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    ) { jsonNode: JsonNode ->
        jsonNode.path("dsUnivCd")
    }

    override fun execute(
        contribution: StepContribution,
        chunkContext: ChunkContext
    ): RepeatStatus? {

        val payloads = PayloadBuilder.toPayload(
            DptGroupPayloadDto(
                year,
                semester
            )
        )
        //TODO: exception handling if response is empty or response code isn't 200
        val responseDtoList: List<DptGroupResponseDto> = client.retrieveAndMap(payloads)

        val query: String = """
                    INSERT INTO dpt_group (dpt_group_id, dpt_group_name)
                    values (?, ?)
                """.trimIndent()

        //TODO: exception handling if jdbc throw exception
        for (dto in responseDtoList) {
            jdbcTemplate.update(query, dto.dptGroupId, dto.dptGroupName)
        }
        return RepeatStatus.FINISHED
    }
}