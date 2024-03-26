package gitp.scrapingbatch.request.objectmapper

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import gitp.scrapingbatch.dto.payload.LecturePayloadDto
import gitp.scrapingbatch.dto.response.LectureIdDto
import gitp.scrapingbatch.dto.response.LectureResponseDto
import gitp.scrapingbatch.dto.response.ProfessorDto
import gitp.scrapingbatch.dto.response.location.OfflineLectureLocationDto
import gitp.scrapingbatch.dto.response.location.OnlineLectureLocationDto
import gitp.scrapingbatch.dto.response.location.PeriodAndLocationDto
import gitp.scrapingbatch.request.TestSampleContainer
import gitp.scrapingbatch.request.YonseiHttpClient
import gitp.scrapingbatch.request.YonseiUrlContainer
import gitp.scrapingbatch.utils.MyUtils
import gitp.type.Day
import gitp.type.OnlineLectureType
import gitp.type.Semester
import gitp.type.YonseiBuilding
import gitp.yonseiprotohttp.payload.PayloadBuilder
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.time.Year

class LectureResponseObjectMapperTest {

    @Test
    fun no_exception_occur_test_by_real_request() {
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

        var cnt = 0
        dtoList.forEach {
            println("###[${++cnt}]")
            println(it)
        }
    }

    @Test
    fun test_by_sample_data() {
        val objectMapper: ObjectMapper = MyUtils.getCommonObjectMapper()

        val lectureResponseDtoList = objectMapper.readValue(
            TestSampleContainer.simpleLectureResponse,
            object : TypeReference<List<LectureResponseDto>>() {})

        Assertions.assertThat(lectureResponseDtoList)
            .contains(
                // TODO: testSampleData contains 4 objects but only wrote assertions for 2
                // objects, add other 2 objects to assertions
                LectureResponseDto(
                    "영화의이해",
                    listOf(
                        ProfessorDto(
                            null,
                            "강철"
                        )
                    ),
                    LectureIdDto(
                        null,
                        "UCE1105",
                        "01",
                        "00"
                    ),
                    listOf(
                        PeriodAndLocationDto(
                            OfflineLectureLocationDto(
                                YonseiBuilding.GWANGBOK,
                                "B105"
                            ),
                            Day.TUE,
                            setOf(2, 3)
                        ),
                        PeriodAndLocationDto(
                            OnlineLectureLocationDto(
                                OnlineLectureType.VIDEO,
                                true
                            ),
                            Day.THU,
                            setOf(1)
                        )
                    )
                ),
                LectureResponseDto(
                    "미학",
                    listOf(
                        ProfessorDto(
                            null,
                            "양희진"
                        )
                    ),
                    LectureIdDto(
                        null,
                        "UCB1110",
                        "01",
                        "00"
                    ),
                    listOf(
                        PeriodAndLocationDto(
                            OfflineLectureLocationDto(
                                YonseiBuilding.EDU,
                                "302"
                            ),
                            Day.MON,
                            setOf(3, 4)
                        ),
                        PeriodAndLocationDto(
                            OnlineLectureLocationDto(
                                OnlineLectureType.VIDEO,
                                true
                            ),
                            Day.WEN,
                            setOf(4)
                        )
                    )
                ),

                LectureResponseDto(
                    "미래를위한교육학",
                    "이무성,장원섭,황금중,박순용,이규민,서영석,이병식,김성원,오석영,류지훈,김남주,하효림,황순예"
                        .split(",")
                        .map {
                            ProfessorDto(null, it)
                        }.toList(),
                    LectureIdDto(
                        null,
                        "EDU2002",
                        "01",
                        "00"
                    ),
                    listOf(
                        PeriodAndLocationDto(
                            OfflineLectureLocationDto(
                                YonseiBuilding.EDU,
                                "306"
                            ),
                            Day.FRI,
                            setOf(6, 7)
                        ),
                        PeriodAndLocationDto(
                            OnlineLectureLocationDto(
                                OnlineLectureType.VIDEO,
                                false
                            ),
                            Day.FRI,
                            setOf(8)
                        )
                    )
                )
            )
    }
}