package gitp.scrapingbatch.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import gitp.scrapingbatch.dto.payload.LecturePayloadDto
import gitp.scrapingbatch.dto.response.LectureResponseDto
import gitp.scrapingbatch.request.YonseiHttpClient
import gitp.scrapingbatch.request.YonseiUrlContainer
import gitp.scrapingbatch.utils.MyUtils
import gitp.type.Semester
import gitp.yonseiprotohttp.payload.PayloadBuilder
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.Year

@SpringBootTest
class LectureResponsePersistServiceTest @Autowired constructor(
    val lectureResponsePersistService: LectureResponsePersistService
) {
    @Test
    fun no_brain_test() {
        val objectMapper: ObjectMapper = MyUtils.getCommonObjectMapper()

        val yonseiHttpClient = YonseiHttpClient.of<LectureResponseDto>(
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
            yonseiHttpClient.retrieveAndMapToList(PayloadBuilder.toPayload(payloadDto))

        for (dto in dtoList) {
            lectureResponsePersistService.save(dto)
        }
    }
}