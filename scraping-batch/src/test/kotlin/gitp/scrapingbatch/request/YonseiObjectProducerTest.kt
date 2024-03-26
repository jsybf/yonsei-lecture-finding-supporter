package gitp.scrapingbatch.request

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import gitp.scrapingbatch.dto.response.LectureIdDto
import gitp.scrapingbatch.dto.response.LectureResponseDto
import gitp.scrapingbatch.dto.response.ProfessorDto
import gitp.scrapingbatch.dto.response.location.OfflineLectureLocationDto
import gitp.scrapingbatch.dto.response.location.OnlineLectureLocationDto
import gitp.scrapingbatch.dto.response.location.PeriodAndLocationDto
import gitp.scrapingbatch.utils.MyUtils
import gitp.type.Day
import gitp.type.OnlineLectureType
import gitp.type.YonseiBuilding
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class YonseiObjectProducerTest {
    @Test
    fun just_execute_test() {
        val objectMapper: ObjectMapper = MyUtils.getCommonObjectMapper()
        val producer: YonseiObjectProducer<LectureResponseDto> =
            YonseiObjectProducer.of<LectureResponseDto>(
                objectMapper
                    .readTree(
                        TestSampleContainer.simpleLectureResponse
                    ) as ArrayNode,
                objectMapper
            )
        val lectureResponseDtoList: MutableList<LectureResponseDto> = mutableListOf()

        while (true) lectureResponseDtoList.add(producer.pop() ?: break)

        assertThat(lectureResponseDtoList)
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