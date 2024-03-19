package gitp.scrapingbatch.request.objectmapper

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import gitp.scrapingbatch.dto.payload.SubjectPayloadDto
import gitp.scrapingbatch.dto.response.LectureIdDto
import gitp.scrapingbatch.dto.response.LectureResponseDto
import gitp.scrapingbatch.dto.response.ProfessorDto
import gitp.scrapingbatch.dto.response.location.OfflineLectureLocationDto
import gitp.scrapingbatch.dto.response.location.OnlineLectureLocationDto
import gitp.scrapingbatch.dto.response.location.PeriodAndLocationDto
import gitp.scrapingbatch.request.YonseiHttpClient
import gitp.type.Day
import gitp.type.OnlineLectureType
import gitp.type.Semester
import gitp.type.YonseiBuilding
import gitp.yonseiprotohttp.payload.PayloadBuilder
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.time.Year

class LectureResponseObjectMapperTest {
    private val url = "https://underwood1.yonsei.ac.kr/sch/sles/SlessyCtr/findAtnlcHandbList.do"

    @Test
    fun no_exception_occur_test_by_real_request() {
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

        var cnt = 0
        dtoList.forEach {
            println("###[${++cnt}]")
            println(it)
        }
    }

    @Test
    fun test_by_sample_data() {
        val objectMapper: ObjectMapper = ObjectMapper()
            .registerKotlinModule()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(
                SimpleModule().addDeserializer(
                    LectureResponseDto::class.java,
                    LectureResponseObjectMapper()
                )
            )

        val lectureResponseDtoList = objectMapper.readValue(
            testSampleData,
            object : TypeReference<List<LectureResponseDto>>() {})

        println(lectureResponseDtoList.size)
        lectureResponseDtoList.forEach { println(it) }
        Assertions.assertThat(lectureResponseDtoList)
            .contains(
                //TODO: testSampleData contains 4 objects but only wrote assertions for 2
                // objects, add other 2 objects to assertions
                LectureResponseDto(
                    "영화의이해",
                    ProfessorDto(
                        null,
                        "강철"
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
                    ProfessorDto(
                        null,
                        "양희진"
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
        }
    ]
    """.trimIndent()
}