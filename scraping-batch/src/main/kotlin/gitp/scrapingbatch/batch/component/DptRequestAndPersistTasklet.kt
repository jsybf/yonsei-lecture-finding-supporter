package gitp.scrapingbatch.batch.component

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import gitp.scrapingbatch.dto.payload.DptPayloadDto
import gitp.scrapingbatch.dto.response.DptGroupResponseDto
import gitp.scrapingbatch.dto.response.DptResponseDto
import gitp.scrapingbatch.request.YonseiHttpClient
import gitp.type.Semester
import gitp.yonseiprotohttp.payload.PayloadBuilder
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.time.Year


class DptRequestAndPersistTasklet(
    private val jdbcTemplate: JdbcTemplate,
) : Tasklet {
    private val requestUrl: String =
        "https://underwood1.yonsei.ac.kr/sch/sles/SlescsCtr/findSchSlesHandbList.do"

    private val client = YonseiHttpClient.of<List<DptResponseDto>>(
        requestUrl,
        ObjectMapper().registerKotlinModule()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    ) { jsonNode: JsonNode ->
        jsonNode.path("dsFaclyCd")
    }

    private var cursor: Int = 1

    private val query: String = """
                    INSERT INTO dpt (dpt_name, dpt_id, dpt_group_id)
                    values (?, ?, ?)
                """.trimIndent()

    val selectQuery: String = """
            SELECT dpt_group_id, dpt_group_name 
            FROM dpt_group
            WHERE id = ?
                """.trimIndent()

    override fun execute(
        contribution: StepContribution, chunkContext: ChunkContext
    ): RepeatStatus? {

        val dptGroupResponseDto: DptGroupResponseDto = jdbcTemplate.query(
            selectQuery, RowMapper<DptGroupResponseDto> { rs: ResultSet, rowNum: Int ->
                DptGroupResponseDto(
                    rs.getString("dpt_group_name"), rs.getString("dpt_group_id")
                )
            }, cursor++
        ).takeIf { it.size == 1 }.let { it!![0] }

        val payloads = PayloadBuilder.toPayload(
            DptPayloadDto(
                dptGroupResponseDto.dptGroupId, Year.of(2024), Semester.FIRST
            )
        )
        // TODO: exception handling if response is empty or response code isn't 200
        val responseDtoList: List<DptResponseDto> = client.retrieveAndMap(payloads)

        // TODO: exception handling if jdbc throw exception
        for (dto in responseDtoList) {
            jdbcTemplate.update(query, dto.dptName, dto.dptId, dptGroupResponseDto.dptGroupId)
            println("insert $dto")
        }
        return RepeatStatus.CONTINUABLE
    }
}