package gitp.scrapingbatch.batch.component

import com.fasterxml.jackson.databind.JsonNode
import gitp.entity.DptGroup
import gitp.scrapingbatch.dto.payload.DptGroupPayloadDto
import gitp.scrapingbatch.dto.response.DptGroupResponseDto
import gitp.scrapingbatch.repository.DptGroupRepository
import gitp.scrapingbatch.request.YonseiHttpClient
import gitp.scrapingbatch.request.YonseiUrlContainer
import gitp.scrapingbatch.utils.MyUtils
import gitp.type.Semester
import gitp.yonseiprotohttp.payload.PayloadBuilder
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import java.time.Year

class DptGroupRequestAndPersistTasklet(
    private val year: Year,
    private val semester: Semester,
    private val dptGroupRepository: DptGroupRepository
) : Tasklet {

    private val client = YonseiHttpClient.of<DptGroupResponseDto>(
        YonseiUrlContainer.dptGroupUrl,
        MyUtils.getCommonObjectMapper()
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
        // TODO: exception handling if response is empty or response code isn't 200
        val responseDtoList: List<DptGroupResponseDto> = client.retrieveAndMapToList(payloads)


        // TODO: exception handling if jdbc throw exception
        for (dto in responseDtoList) {
            dptGroupRepository.save(
                DptGroup(
                    null,
                    dto.dptGroupName,
                    dto.dptGroupId
                )
            )
        }
        return RepeatStatus.FINISHED
    }
}