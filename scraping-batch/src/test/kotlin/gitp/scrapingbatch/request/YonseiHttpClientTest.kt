package gitp.scrapingbatch.request

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import gitp.scrapingbatch.dto.response.LectureResponseDto
import gitp.scrapingbatch.request.objectmapper.LectureResponseObjectMapper
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class YonseiHttpClientTest {
    private val objectMapper: ObjectMapper = ObjectMapper()
        .registerKotlinModule()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .registerModule(
            SimpleModule().addDeserializer(
                LectureResponseDto::class.java,
                LectureResponseObjectMapper()
            )
        )
    private val url = "https://underwood1.yonsei.ac.kr/sch/sles/SlessyCtr/findAtnlcHandbList.do"

    @Test
    fun skip_predicate_test() {
        val client = YonseiHttpClient.of<List<LectureResponseDto>>(
            url,
            objectMapper,
            mapOf("rmvlcYnNm" to "폐강"),
            null
        )

        val refineJson: ArrayNode =
            client.refineJson(objectMapper.readTree(sampleDataContainsClosedLecture))
        println(refineJson.size())
        Assertions.assertThat(refineJson).hasSize(1)
    }

    // contains three lectures while two closedLecture included
    private val sampleDataContainsClosedLecture = """
      [
        {
            "lawscSubjcgpNm": null,
            "srclnLctreLangDivCd": null,
            "coprtEstblYn": "0",
            "smtDivCd": "10",
            "lessnSessnDivCd": "A",
            "lctreTimeNm": "수10",
            "lawscSubjcFldNm": null,
            "corseDvclsNo": "04",
            "hy": "0,2,3,4,5",
            "subsrtDivCd": "FP",
            "subjtnb": "YCA1003",
            "experPrctsAmt": 0,
            "subjtSbtlNm": null,
            "subjtClNm": "대면강의",
            "campsDivNm": "신촌",
            "syllaUnregTrgetDivCd": "0",
            "subjtChngGudncDivCdTm": null,
            "onppsPrttnAmt": 0,
            "subjtChngGudncDivCdPl": null,
            "gradeEvlMthdDivNm": "P/NP",
            "cdt": 0,
            "srclnLctreYn": "0",
            "rcognHrs": 1,
            "cgprfNm": null,
            "subjtChngGudncDivCdPr": null,
            "timtbDplctPermKindCd": "10",
            "atntnMattrDesc": null,
            "usubjtnb": "U00001",
            "lctreTimeEngNm": "Wed10",
            "rmvlcYn": "1",
            "excstPercpFg": "0",
            "subjtNm": "채플(3)",
            "lecrmNm": null,
            "rmvlcYnNm": "폐강",
            "medcHyLisup": null,
            "gradeEvlMthdDivCd": "3",
            "estblDeprtCd": "30105",
            "subjtNm2": "채플(3)",
            "syy": "2024",
            "prctsCorseDvclsNo": "00",
            "campsBusnsCd": "s1",
            "cgprfEngNm": null,
            "syySmtDivNm": "2024-1학기",
            "estblDeprtOrd": 40,
            "subjtnbCorsePrcts": "YCA1003-04-00",
            "subjtUnitVal": "1000",
            "srclnLctreLangDivNm": null,
            "cgprfNndsYn": "0",
            "estblDeprtNm": "교양기초(2019학번~) 채플",
            "subjtEngNm": "CHAPEL(3)",
            "orgSysinstDivCd": "H1",
            "lecrmEngNm": null,
            "lessnSessnDivNm": "학기",
            "subjtSbtlEngNm": null,
            "attflUuid": null,
            "cmptPrctsAmt": 0,
            "tmtcYn": "0",
            "subsrtDivNm": "교기",
            "sysinstDivCd": "H1",
            "lawscSubjcChrtzNm": null
        },
        {
            "lawscSubjcgpNm": null,
            "srclnLctreLangDivCd": null,
            "coprtEstblYn": "0",
            "smtDivCd": "10",
            "lessnSessnDivCd": "A",
            "lctreTimeNm": "수10",
            "lawscSubjcFldNm": null,
            "corseDvclsNo": "04",
            "hy": "0,3,2,4,5",
            "subsrtDivCd": "FP",
            "subjtnb": "YCA1007",
            "experPrctsAmt": 0,
            "subjtSbtlNm": null,
            "subjtClNm": "대면강의",
            "campsDivNm": "신촌",
            "syllaUnregTrgetDivCd": "0",
            "subjtChngGudncDivCdTm": null,
            "onppsPrttnAmt": 0,
            "subjtChngGudncDivCdPl": null,
            "gradeEvlMthdDivNm": "P/NP",
            "cdt": 0.5,
            "srclnLctreYn": "0",
            "rcognHrs": 1,
            "cgprfNm": null,
            "subjtChngGudncDivCdPr": null,
            "timtbDplctPermKindCd": "10",
            "atntnMattrDesc": null,
            "usubjtnb": "U00001",
            "lctreTimeEngNm": "Wed10",
            "rmvlcYn": "1",
            "excstPercpFg": "0",
            "subjtNm": "채플(C)",
            "lecrmNm": null,
            "rmvlcYnNm": "폐강",
            "medcHyLisup": null,
            "gradeEvlMthdDivCd": "3",
            "estblDeprtCd": "30105",
            "subjtNm2": "채플(C)",
            "syy": "2024",
            "prctsCorseDvclsNo": "00",
            "campsBusnsCd": "s1",
            "cgprfEngNm": null,
            "syySmtDivNm": "2024-1학기",
            "estblDeprtOrd": 40,
            "subjtnbCorsePrcts": "YCA1007-04-00",
            "subjtUnitVal": "1000",
            "srclnLctreLangDivNm": null,
            "cgprfNndsYn": "0",
            "estblDeprtNm": "교양기초(2019학번~) 채플",
            "subjtEngNm": "CHAPEL(C)",
            "orgSysinstDivCd": "H1",
            "lecrmEngNm": null,
            "lessnSessnDivNm": "학기",
            "subjtSbtlEngNm": null,
            "attflUuid": null,
            "cmptPrctsAmt": 0,
            "tmtcYn": "0",
            "subsrtDivNm": "교기",
            "sysinstDivCd": "H1",
            "lawscSubjcChrtzNm": null
        },
        {
            "lawscSubjcgpNm": null,
            "srclnLctreLangDivCd": null,
            "coprtEstblYn": "0",
            "smtDivCd": "10",
            "lessnSessnDivCd": "A",
            "lctreTimeNm": "일1",
            "lawscSubjcFldNm": null,
            "corseDvclsNo": "02",
            "hy": "0,2,3,4,5",
            "subsrtDivCd": "FP",
            "subjtnb": "YCA1003",
            "experPrctsAmt": 0,
            "subjtSbtlNm": "(비대면)",
            "subjtClNm": "비대면(동영상) ",
            "campsDivNm": "신촌",
            "syllaUnregTrgetDivCd": "0",
            "subjtChngGudncDivCdTm": null,
            "onppsPrttnAmt": 0,
            "subjtChngGudncDivCdPl": null,
            "gradeEvlMthdDivNm": "P/NP",
            "cdt": 0,
            "srclnLctreYn": "0",
            "rcognHrs": 1,
            "cgprfNm": "정미현,김동환",
            "subjtChngGudncDivCdPr": "1",
            "timtbDplctPermKindCd": "10",
            "atntnMattrDesc": "초과학기생 수강가능",
            "usubjtnb": "U00001",
            "lctreTimeEngNm": "Sun1",
            "rmvlcYn": "0",
            "excstPercpFg": "0",
            "subjtNm": "채플(3)(비대면)",
            "lecrmNm": "동영상콘텐츠",
            "rmvlcYnNm": " ",
            "medcHyLisup": null,
            "gradeEvlMthdDivCd": "3",
            "estblDeprtCd": "30105",
            "subjtNm2": "채플(3)",
            "syy": "2024",
            "prctsCorseDvclsNo": "00",
            "campsBusnsCd": "s1",
            "cgprfEngNm": "Chung Meehyun/Dong Hwan Kim",
            "syySmtDivNm": "2024-1학기",
            "estblDeprtOrd": 40,
            "subjtnbCorsePrcts": "YCA1003-02-00",
            "subjtUnitVal": "1000",
            "srclnLctreLangDivNm": null,
            "cgprfNndsYn": "0",
            "estblDeprtNm": "교양기초(2019학번~) 채플",
            "subjtEngNm": "CHAPEL(3)",
            "orgSysinstDivCd": "H1",
            "lecrmEngNm": "Pre-recorded lecture",
            "lessnSessnDivNm": "학기",
            "subjtSbtlEngNm": null,
            "attflUuid": null,
            "cmptPrctsAmt": 0,
            "tmtcYn": "1",
            "subsrtDivNm": "교기",
            "sysinstDivCd": "H1",
            "lawscSubjcChrtzNm": null
        }
      ]
    """.trimIndent()
}