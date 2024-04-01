package gitp.scrapingbatch.request.objectmapper

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import gitp.scrapingbatch.dto.response.MileageResponseDto
import gitp.type.Fraction
import gitp.type.MajorType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MileageResponseObjectMapperTest {

    @Test
    fun no_exception_occur_test_by_real_request() {
        val objectMapper: ObjectMapper = ObjectMapper()
            .registerKotlinModule()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(
                SimpleModule().addDeserializer(
                    MileageResponseDto::class.java,
                    MileageResponseObjectMapper()
                )
            )

        val dtoList: List<MileageResponseDto> = objectMapper.readValue(
            testSample,
            object : TypeReference<List<MileageResponseDto>>() {}
        )

        dtoList.forEach { println(it) }
        assertThat(dtoList)
            .containsOnly(
                MileageResponseDto(
                    0,
                    12,
                    true,
                    2,
                    MajorType.MAJOR,
                    Fraction(40, 126),
                    1.0,
                    6,
                    true,
                    false
                ),
                MileageResponseDto(
                    1,
                    12,
                    true,
                    4,
                    MajorType.MAJOR,
                    Fraction(129, 126),
                    0.7222,
                    6,
                    ifFirstRegister = true,
                    ifGraduateApplied = true
                )
            )
    }

    private val testSample: String = """
        [
                {
                    "ttCmpsjCdtRto": 0.3174,
                    "jstbfSmtCmpsjAtnlcPosblCdt": "23/18",
                    "remrk": "* ",
                    "aplySubjcCnt": 6,
                    "mlgRank": 0,
                    "mlgAppcsPrcesDivNm": "Y",
                    "hy": "2",
                    "fratlcYn": "Y",
                    "grdtnAplyYn": "N",
                    "jstbfSmtCmpsjCdtRto": 1,
                    "mjsbjYn": "Y(Y)",
                    "ttCmpsjGrdtnCmpsjCdt": "40/126",
                    "mlgVal": 12,
                    "dsstdYn": "Y"
                },
                {
                    "ttCmpsjCdtRto": 1,
                    "jstbfSmtCmpsjAtnlcPosblCdt": "13/18",
                    "remrk": null,
                    "aplySubjcCnt": 6,
                    "mlgRank": 1,
                    "mlgAppcsPrcesDivNm": "Y",
                    "hy": "4",
                    "fratlcYn": "Y",
                    "grdtnAplyYn": "Y",
                    "jstbfSmtCmpsjCdtRto": 0.7222,
                    "mjsbjYn": "Y(Y)",
                    "ttCmpsjGrdtnCmpsjCdt": "129/126",
                    "mlgVal": 12,
                    "dsstdYn": "N"
                }
        ]
    """.trimIndent()

}