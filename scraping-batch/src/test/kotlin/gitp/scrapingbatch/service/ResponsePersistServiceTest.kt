package gitp.scrapingbatch.service

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import gitp.scrapingbatch.dto.payload.SubjectPayloadDto
import gitp.scrapingbatch.dto.response.LectureResponseDto
import gitp.scrapingbatch.request.YonseiHttpClient
import gitp.scrapingbatch.request.objectmapper.LectureResponseObjectMapper
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
        val url = "https://underwood1.yonsei.ac.kr/sch/sles/SlessyCtr/findAtnlcHandbList.do"
        val objectMapper: ObjectMapper = ObjectMapper()
            .registerKotlinModule()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(
                SimpleModule().addDeserializer(
                    LectureResponseDto::class.java,
                    LectureResponseObjectMapper()
                )
            )

        val yonseiHttpClient: YonseiHttpClient<List<LectureResponseDto>> =
            YonseiHttpClient.of<List<LectureResponseDto>>(
                url,
                objectMapper
            ) { jsonNode: JsonNode ->
                jsonNode.path("dsSles251")
            }

        val payloadDto: SubjectPayloadDto = SubjectPayloadDto(
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