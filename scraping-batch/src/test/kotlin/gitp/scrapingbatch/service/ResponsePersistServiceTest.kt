package gitp.scrapingbatch.service

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import gitp.scrapingbatch.dto.payload.LecturePayloadDto
import gitp.scrapingbatch.dto.response.LectureResponseDto
import gitp.scrapingbatch.request.YonseiHttpClient
import gitp.scrapingbatch.request.YonseiUrlContainer
import gitp.scrapingbatch.request.objectmapper.LectureResponseObjectMapper
import gitp.scrapingbatch.utils.MyUtils
import gitp.type.Semester
import gitp.yonseiprotohttp.payload.PayloadBuilder
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.Year

@SpringBootTest
class ResponsePersistServiceTest @Autowired constructor(
    val responsePersistService: ResponsePersistService
) {
    @Test
    fun no_brain_test() {
        val objectMapper: ObjectMapper = MyUtils.getCommonObjectMapper()

        val yonseiHttpClient: YonseiHttpClient<List<LectureResponseDto>> =
            YonseiHttpClient.of<List<LectureResponseDto>>(
                YonseiUrlContainer.lectureUrl,
                objectMapper
            ) { jsonNode: JsonNode ->
                jsonNode.path("dsSles251")
            }

        val payloadDto: LecturePayloadDto = LecturePayloadDto(
            Year.of(2024),
            Semester.FIRST,
            "s11001",
            "30111"
        )

        val dtoList: List<LectureResponseDto> =
            yonseiHttpClient.retrieveAndMap(PayloadBuilder.toPayload(payloadDto))

        for (dto in dtoList) {
            responsePersistService.save(dto)
        }
    }
}