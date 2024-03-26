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

        val yonseiHttpClient = YonseiHttpClient.of<List<LectureResponseDto>>(
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
            testSampleData,
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

    private val testSampleData = """
        [
        {
            "lawscSubjcgpNm": null,
            "srclnLctreLangDivCd": null,
            "coprtEstblYn": "0",
            "smtDivCd": "10",
            "lessnSessnDivCd": "A",
            "lctreTimeNm": "월3,4/수4",
            "lawscSubjcFldNm": null,
            "corseDvclsNo": "01",
            "hy": "0",
            "subsrtDivCd": "FR",
            "subjtnb": "UCB1110",
            "experPrctsAmt": 0,
            "subjtSbtlNm": null,
            "subjtClNm": "블랜디드(동영상) ",
            "campsDivNm": "신촌",
            "syllaUnregTrgetDivCd": "0",
            "subjtChngGudncDivCdTm": null,
            "onppsPrttnAmt": 0,
            "subjtChngGudncDivCdPl": null,
            "gradeEvlMthdDivNm": "절대평가",
            "cdt": 3,
            "srclnLctreYn": "0",
            "rcognHrs": 3,
            "cgprfNm": "양희진",
            "subjtChngGudncDivCdPr": null,
            "timtbDplctPermKindCd": null,
            "atntnMattrDesc": null,
            "usubjtnb": "U00088",
            "lctreTimeEngNm": "Mon3,4/Wed4",
            "rmvlcYn": "0",
            "excstPercpFg": "1",
            "subjtNm": "미학",
            "lecrmNm": "교302/동영상콘텐츠",
            "rmvlcYnNm": " ",
            "medcHyLisup": null,
            "gradeEvlMthdDivCd": "1",
            "estblDeprtCd": "30109",
            "subjtNm2": "미학",
            "syy": "2024",
            "prctsCorseDvclsNo": "00",
            "campsBusnsCd": "s1",
            "cgprfEngNm": "Yang HeeJin",
            "syySmtDivNm": "2024-1학기",
            "estblDeprtOrd": 90,
            "subjtnbCorsePrcts": "UCB1110-01-00",
            "subjtUnitVal": "1000",
            "srclnLctreLangDivNm": null,
            "cgprfNndsYn": "0",
            "estblDeprtNm": "대학교양(2019학번~) 문학과예술",
            "subjtEngNm": "AESTHETICS",
            "orgSysinstDivCd": "H1",
            "lecrmEngNm": null,
            "lessnSessnDivNm": "학기",
            "subjtSbtlEngNm": null,
            "attflUuid": null,
            "cmptPrctsAmt": 0,
            "tmtcYn": "0",
            "subsrtDivNm": "대교",
            "sysinstDivCd": "H1",
            "lawscSubjcChrtzNm": null
        },
        {
            "lawscSubjcgpNm": null,
            "srclnLctreLangDivCd": null,
            "coprtEstblYn": "0",
            "smtDivCd": "10",
            "lessnSessnDivCd": "A",
            "lctreTimeNm": "화2,3/목1",
            "lawscSubjcFldNm": null,
            "corseDvclsNo": "01",
            "hy": "0",
            "subsrtDivCd": "FR",
            "subjtnb": "UCE1105",
            "experPrctsAmt": 0,
            "subjtSbtlNm": null,
            "subjtClNm": "블랜디드(동영상) ",
            "campsDivNm": "신촌",
            "syllaUnregTrgetDivCd": "0",
            "subjtChngGudncDivCdTm": null,
            "onppsPrttnAmt": 0,
            "subjtChngGudncDivCdPl": null,
            "gradeEvlMthdDivNm": "절대평가",
            "cdt": 3,
            "srclnLctreYn": "0",
            "rcognHrs": 3,
            "cgprfNm": "강철",
            "subjtChngGudncDivCdPr": null,
            "timtbDplctPermKindCd": null,
            "atntnMattrDesc": null,
            "usubjtnb": "U00283",
            "lctreTimeEngNm": "Tue2,3/Thu1",
            "rmvlcYn": "0",
            "excstPercpFg": "1",
            "subjtNm": "영화의이해",
            "lecrmNm": "광B105/동영상콘텐츠",
            "rmvlcYnNm": " ",
            "medcHyLisup": null,
            "gradeEvlMthdDivCd": "1",
            "estblDeprtCd": "30109",
            "subjtNm2": "영화의이해",
            "syy": "2024",
            "prctsCorseDvclsNo": "00",
            "campsBusnsCd": "s1",
            "cgprfEngNm": "Kang  Chul",
            "syySmtDivNm": "2024-1학기",
            "estblDeprtOrd": 90,
            "subjtnbCorsePrcts": "UCE1105-01-00",
            "subjtUnitVal": "1000",
            "srclnLctreLangDivNm": null,
            "cgprfNndsYn": "0",
            "estblDeprtNm": "대학교양(2019학번~) 문학과예술",
            "subjtEngNm": "UNDERSTANDING ON CINEMA",
            "orgSysinstDivCd": "H1",
            "lecrmEngNm": null,
            "lessnSessnDivNm": "학기",
            "subjtSbtlEngNm": null,
            "attflUuid": null,
            "cmptPrctsAmt": 0,
            "tmtcYn": "0",
            "subsrtDivNm": "대교",
            "sysinstDivCd": "H1",
            "lawscSubjcChrtzNm": null
        },
        {
            "lawscSubjcgpNm": null,
            "srclnLctreLangDivCd": null,
            "coprtEstblYn": "0",
            "smtDivCd": "10",
            "lessnSessnDivCd": "A",
            "lctreTimeNm": "수5,금5,6",
            "lawscSubjcFldNm": null,
            "corseDvclsNo": "02",
            "hy": "0",
            "subsrtDivCd": "FR",
            "subjtnb": "UCE1105",
            "experPrctsAmt": 0,
            "subjtSbtlNm": null,
            "subjtClNm": "대면강의",
            "campsDivNm": "신촌",
            "syllaUnregTrgetDivCd": "0",
            "subjtChngGudncDivCdTm": null,
            "onppsPrttnAmt": 0,
            "subjtChngGudncDivCdPl": null,
            "gradeEvlMthdDivNm": "절대평가",
            "cdt": 3,
            "srclnLctreYn": "0",
            "rcognHrs": 3,
            "cgprfNm": "김동호",
            "subjtChngGudncDivCdPr": null,
            "timtbDplctPermKindCd": null,
            "atntnMattrDesc": null,
            "usubjtnb": "U00283",
            "lctreTimeEngNm": "Wed5,Fri5,6",
            "rmvlcYn": "0",
            "excstPercpFg": "1",
            "subjtNm": "영화의이해",
            "lecrmNm": "광B105",
            "rmvlcYnNm": " ",
            "medcHyLisup": null,
            "gradeEvlMthdDivCd": "1",
            "estblDeprtCd": "30109",
            "subjtNm2": "영화의이해",
            "syy": "2024",
            "prctsCorseDvclsNo": "00",
            "campsBusnsCd": "s1",
            "cgprfEngNm": "KIM DONG HO",
            "syySmtDivNm": "2024-1학기",
            "estblDeprtOrd": 90,
            "subjtnbCorsePrcts": "UCE1105-02-00",
            "subjtUnitVal": "1000",
            "srclnLctreLangDivNm": null,
            "cgprfNndsYn": "0",
            "estblDeprtNm": "대학교양(2019학번~) 문학과예술",
            "subjtEngNm": "UNDERSTANDING ON CINEMA",
            "orgSysinstDivCd": "H1",
            "lecrmEngNm": null,
            "lessnSessnDivNm": "학기",
            "subjtSbtlEngNm": null,
            "attflUuid": null,
            "cmptPrctsAmt": 0,
            "tmtcYn": "0",
            "subsrtDivNm": "대교",
            "sysinstDivCd": "H1",
            "lawscSubjcChrtzNm": null
        },
        {
            "lawscSubjcgpNm": null,
            "srclnLctreLangDivCd": null,
            "coprtEstblYn": "0",
            "smtDivCd": "10",
            "lessnSessnDivCd": "A",
            "lctreTimeNm": "수3/금3,4",
            "lawscSubjcFldNm": null,
            "corseDvclsNo": "01",
            "hy": "0",
            "subsrtDivCd": "FR",
            "subjtnb": "UCE1107",
            "experPrctsAmt": 0,
            "subjtSbtlNm": null,
            "subjtClNm": "블랜디드(동영상) ",
            "campsDivNm": "신촌",
            "syllaUnregTrgetDivCd": "0",
            "subjtChngGudncDivCdTm": null,
            "onppsPrttnAmt": 0,
            "subjtChngGudncDivCdPl": null,
            "gradeEvlMthdDivNm": "절대평가",
            "cdt": 3,
            "srclnLctreYn": "0",
            "rcognHrs": 3,
            "cgprfNm": "지형주",
            "subjtChngGudncDivCdPr": null,
            "timtbDplctPermKindCd": null,
            "atntnMattrDesc": "음악대학 소속학생 수강불가",
            "usubjtnb": "U00287",
            "lctreTimeEngNm": "Wed3/Fri3,4",
            "rmvlcYn": "0",
            "excstPercpFg": "1",
            "subjtNm": "음악사",
            "lecrmNm": "교102/동영상콘텐츠",
            "rmvlcYnNm": " ",
            "medcHyLisup": null,
            "gradeEvlMthdDivCd": "1",
            "estblDeprtCd": "30109",
            "subjtNm2": "음악사",
            "syy": "2024",
            "prctsCorseDvclsNo": "00",
            "campsBusnsCd": "s1",
            "cgprfEngNm": "Chi Hyungjoo",
            "syySmtDivNm": "2024-1학기",
            "estblDeprtOrd": 90,
            "subjtnbCorsePrcts": "UCE1107-01-00",
            "subjtUnitVal": "1000",
            "srclnLctreLangDivNm": null,
            "cgprfNndsYn": "0",
            "estblDeprtNm": "대학교양(2019학번~) 문학과예술",
            "subjtEngNm": "HISTORY OF MUSIC",
            "orgSysinstDivCd": "H1",
            "lecrmEngNm": null,
            "lessnSessnDivNm": "학기",
            "subjtSbtlEngNm": null,
            "attflUuid": null,
            "cmptPrctsAmt": 0,
            "tmtcYn": "0",
            "subsrtDivNm": "대교",
            "sysinstDivCd": "H1",
            "lawscSubjcChrtzNm": null
        },
        {
            "lawscSubjcgpNm": null,
            "srclnLctreLangDivCd": null,
            "coprtEstblYn": "0",
            "smtDivCd": "10",
            "lessnSessnDivCd": "A",
            "lctreTimeNm": "금6,7/금8",
            "lawscSubjcFldNm": null,
            "corseDvclsNo": "01",
            "hy": "2",
            "subsrtDivCd": "FR",
            "subjtnb": "EDU2002",
            "experPrctsAmt": 0,
            "subjtSbtlNm": null,
            "subjtClNm": "블랜디드(동영상) ",
            "campsDivNm": "신촌",
            "syllaUnregTrgetDivCd": "0",
            "subjtChngGudncDivCdTm": null,
            "onppsPrttnAmt": 0,
            "subjtChngGudncDivCdPl": "1",
            "gradeEvlMthdDivNm": "절대평가",
            "cdt": 3,
            "srclnLctreYn": "0",
            "rcognHrs": 3,
            "cgprfNm": "이무성,장원섭,황금중,박순용,이규민,서영석,이병식,김성원,오석영,류지훈,김남주,하효림,황순예",
            "subjtChngGudncDivCdPr": "1",
            "timtbDplctPermKindCd": null,
            "atntnMattrDesc": "교육학부 전공필수 및 필수교양(대학) 교차인정 가능",
            "usubjtnb": "J00055",
            "lctreTimeEngNm": "Fri6,7/Fri8",
            "rmvlcYn": "0",
            "excstPercpFg": "0",
            "subjtNm": "미래를위한교육학",
            "lecrmNm": "교306/동영상(중복수강불가)",
            "rmvlcYnNm": " ",
            "medcHyLisup": null,
            "gradeEvlMthdDivCd": "1",
            "estblDeprtCd": "30113",
            "subjtNm2": "미래를위한교육학",
            "syy": "2024",
            "prctsCorseDvclsNo": "00",
            "campsBusnsCd": "s1",
            "cgprfEngNm": "Lee Moosung/Chang, Wonsup/Hwang Keumjoong/Pak Soon-Yong/LEE GUEMIN/Seo Young Seok/Rhee Byung Shik/Kim Sung won/Oh Seok Young/Ryoo Ji Hoon/Kim Nam Ju/HA HYO RIM/Hwang Soon Ye",
            "syySmtDivNm": "2024-1학기",
            "estblDeprtOrd": 130,
            "subjtnbCorsePrcts": "EDU2002-01-00",
            "subjtUnitVal": "2000",
            "srclnLctreLangDivNm": null,
            "cgprfNndsYn": "0",
            "estblDeprtNm": "대학교양(2019학번~) 국가와사회",
            "subjtEngNm": "EDUCATION FOR TOMORROW",
            "orgSysinstDivCd": "H1",
            "lecrmEngNm": "Edu306/Pre-recorded lecture(Unable to take other class)",
            "lessnSessnDivNm": "학기",
            "subjtSbtlEngNm": null,
            "attflUuid": null,
            "cmptPrctsAmt": 0,
            "tmtcYn": "1",
            "subsrtDivNm": "대교",
            "sysinstDivCd": "H1",
            "lawscSubjcChrtzNm": null
        }
    ]
    """.trimIndent()
}