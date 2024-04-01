package gitp.yonseiprotohttp.payload

import gitp.scrapingbatch.dto.payload.*
import gitp.scrapingbatch.dto.response.LectureIdDto
import gitp.type.Semester
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.time.Year

class PayloadBuilderTest {

    /**
     * @return: first: input /  second: expected
     */
    fun comparePayloadImporved(
        expectedPayload: String,
        inputDto: PayloadDto
    ): Pair<Map<String, String>,
            Map<String, String>> {
        // execute
        val inputPayload = PayloadBuilder.toPayload(inputDto)

        // compare targetPayload and payload by converting them to map
        val expectedPayloadMap: Map<String, String> = expectedPayload.split("&")
            .map { it.split("=") }
            .associateBy({ it[0] }, { it[1] })
        val inputPayloadMap: Map<String, String> = inputPayload.split("&")
            .map { it.split("=") }
            .associateBy({ it[0] }, { it[1] })

        return Pair(inputPayloadMap, expectedPayloadMap)
    }

    @Test
    fun department_group_payload_build_test() {
        val expectedPayload: String =
            "_menuId=MTA5MzM2MTI3MjkzMTI2NzYwMDA%3D&_menuNm=&_pgmId=NDE0MDA4NTU1NjY%3D&%40d1%23dsNm=dsUnivCd&%40d1%23level=B&%40d1%23lv1=s1&%40d1%23lv2=%25&%40d1%23lv3=%25&%40d1%23sysinstDivCd=%25&%40d1%23univGbn=A&%40d1%23findAuthGbn=8&%40d1%23syy=2024&%40d1%23smtDivCd=10&%40d%23=%40d1%23&%40d1%23=dmCond&%40d1%23tp=dm"

        val dto = DptGroupPayloadDto(
            Year.of(2024),
            Semester.FIRST
        )

        val (input, expected) = comparePayloadImporved(expectedPayload, dto)
        Assertions.assertThat(input).containsExactlyInAnyOrderEntriesOf(expected)
    }

    @Test
    fun department_payload_build_test() {
        val expectedPayload: String =
            "_menuId=MTA5MzM2MTI3MjkzMTI2NzYwMDA%3D&_menuNm=&_pgmId=NDE0MDA4NTU1NjY%3D&%40d1%23dsNm=dsFaclyCd&%40d1%23level=B&%40d1%23lv1=s1&%40d1%23lv2=s11001&%40d1%23lv3=%25&%40d1%23sysinstDivCd=%25&%40d1%23univGbn=A&%40d1%23findAuthGbn=8&%40d1%23syy=2024&%40d1%23smtDivCd=10&%40d%23=%40d1%23&%40d1%23=dmCond&%40d1%23tp=dm"

        val dto = DptPayloadDto(
            "s11001",
            Year.of(2024),
            Semester.FIRST
        )

        val (input, expected) = comparePayloadImporved(expectedPayload, dto)
        Assertions.assertThat(input).containsExactlyInAnyOrderEntriesOf(expected)
    }

    @Test
    fun subject_payload_build_test() {
        val expectedPayload: String =
            "_menuId=MTA5MzM2MTI3MjkzMTI2NzYwMDA%3D&_menuNm=&_pgmId=NDE0MDA4NTU1NjY%3D&%40d1" +
                    "%23syy=2024&%40d1%23smtDivCd=10&%40d1%23campsBusnsCd=s1&%40d1%23univCd=s11001&%40d1%23faclyCd=30109&%40d1%23hy=&%40d1%23cdt=%25&%40d1%23kwdDivCd=1&%40d1%23searchGbn=1&%40d1%23kwd=&%40d1%23allKwd=&%40d1%23engChg=&%40d1%23prnGbn=false&%40d1%23lang=&%40d1%23campsDivCd=S&%40d1%23stuno=&%40d%23=%40d1%23&%40d1%23=dmCond&%40d1%23tp=dm"

        val dto = LecturePayloadDto(
            Year.of(2024),
            Semester.FIRST,
            "s11001",
            "30109"
        )

        val (input, expected) = comparePayloadImporved(expectedPayload, dto)
        Assertions.assertThat(input).containsExactlyInAnyOrderEntriesOf(expected)
    }


    @Test
    fun mileage_payload_build_test() {

        val expectedPayload: String =
            "_menuId=MTA5MzM2MTI3MjkzMTI2NzYwMDA%3D&_menuNm=&_pgmId=NDE0MDA4NTU1NjY%3D&%40d1%23sysinstDivCd=H1&%40d1%23syy=2024&%40d1%23smtDivCd=10&%40d1%23stuno=&%40d1%23subjtnb=ECO1101&%40d1%23appcsSchdlCd=&%40d1%23corseDvclsNo=01&%40d1%23prctsCorseDvclsNo=00&%40d%23=%40d1%23&%40d1%23=dmCond&%40d1%23tp=dm"

        val dto = MileagePayloadDto(
            Year.of(2024),
            Semester.FIRST,
            LectureIdDto(
                null,
                "ECO1101",
                "01",
                "00"
            )
        )

        val (input, expected) = comparePayloadImporved(expectedPayload, dto)
        Assertions.assertThat(input).containsExactlyInAnyOrderEntriesOf(expected)
    }

    @Test
    fun mileage_summary_payload_build_test() {

        val expectedPayload: String =
            "_menuId=MTA5MzM2MTI3MjkzMTI2NzYwMDA%3D&_menuNm=&_pgmId=NDE0MDA4NTU1NjY%3D&%40d1%23syy=2024&%40d1%23smtDivCd=10&%40d1%23sysinstDivCd=H1&%40d1%23subjtnb=UCE1105&%40d1%23corseDvclsNo=01&%40d1%23prctsCorseDvclsNo=00&%40d1%23syySmtDivCd=202410&%40d%23=%40d1%23&%40d1%23=dmCond&%40d1%23tp=dm"

        val dto = MileageSummaryPayloadDto(
            Year.of(2024),
            Semester.FIRST,
            LectureIdDto(
                null,
                "UCE1105",
                "01",
                "00"
            )
        )

        val (input, expected) = comparePayloadImporved(expectedPayload, dto)
        Assertions.assertThat(input).containsExactlyInAnyOrderEntriesOf(expected)
    }
}