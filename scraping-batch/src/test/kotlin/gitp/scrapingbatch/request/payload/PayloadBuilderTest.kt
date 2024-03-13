package gitp.yonseiprotohttp.payload

import gitp.yonseiprotohttp.payload.dto.DptGroupPayloadDto
import gitp.yonseiprotohttp.payload.dto.DptPayloadDto
import gitp.yonseiprotohttp.payload.dto.PayloadDto
import gitp.yonseiprotohttp.payload.dto.SubjectPayloadDto
import gitp.yonseiprotohttp.payload.type.Semester
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.time.Year

class PayloadBuilderTest {

    fun comparePayload(targetPayload: String, dto: PayloadDto): Boolean {
        //execute
        val payload = PayloadBuilder.toPayload(dto)

        //compare targetPayload and payload by converting them to map
        val targetPayloadMap: Map<String, String> = targetPayload.split("&")
            .map { it.split("=") }
            .associateBy({ it[0] }, { it[1] })
        val payloadMap: Map<String, String> = payload.split("&")
            .map { it.split("=") }
            .associateBy({ it[0] }, { it[1] })

        return targetPayloadMap == payloadMap
    }

    @Test
    fun department_group_payload_build_test() {
        val targetPayload: String =
            "_menuId=MTA5MzM2MTI3MjkzMTI2NzYwMDA%3D&_menuNm=&_pgmId=NDE0MDA4NTU1NjY%3D&%40d1%23dsNm=dsUnivCd&%40d1%23level=B&%40d1%23lv1=s1&%40d1%23lv2=%25&%40d1%23lv3=%25&%40d1%23sysinstDivCd=%25&%40d1%23univGbn=A&%40d1%23findAuthGbn=8&%40d1%23syy=2024&%40d1%23smtDivCd=10&%40d%23=%40d1%23&%40d1%23=dmCond&%40d1%23tp=dm"

        val dto = DptGroupPayloadDto(
            Year.of(2024),
            Semester.FIRST
        )

        Assertions.assertThat(comparePayload(targetPayload, dto)).isTrue()
    }

    @Test
    fun department_payload_build_test() {
        val targetPayload: String =
            "_menuId=MTA5MzM2MTI3MjkzMTI2NzYwMDA%3D&_menuNm=&_pgmId=NDE0MDA4NTU1NjY%3D&%40d1%23dsNm=dsFaclyCd&%40d1%23level=B&%40d1%23lv1=s1&%40d1%23lv2=s11001&%40d1%23lv3=%25&%40d1%23sysinstDivCd=%25&%40d1%23univGbn=A&%40d1%23findAuthGbn=8&%40d1%23syy=2024&%40d1%23smtDivCd=10&%40d%23=%40d1%23&%40d1%23=dmCond&%40d1%23tp=dm"

        val dto = DptPayloadDto(
            "s11001",
            Year.of(2024),
            Semester.FIRST
        )

        Assertions.assertThat(comparePayload(targetPayload, dto)).isTrue()
    }

    @Test
    fun subject_payload_build_test() {
        val targetPayload: String =
            "_menuId=MTA5MzM2MTI3MjkzMTI2NzYwMDA%3D&_menuNm=&_pgmId=NDE0MDA4NTU1NjY%3D&%40d1%23syy=2024&%40d1%23smtDivCd=10&%40d1%23campsBusnsCd=s1&%40d1%23univCd=s11001&%40d1%23faclyCd=30109&%40d1%23hy=&%40d1%23cdt=%25&%40d1%23kwdDivCd=1&%40d1%23searchGbn=1&%40d1%23kwd=&%40d1%23allKwd=&%40d1%23engChg=&%40d1%23prnGbn=false&%40d1%23lang=&%40d1%23campsDivCd=&%40d1%23stuno=&%40d%23=%40d1%23&%40d1%23=dmCond&%40d1%23tp=dm"

        val dto = SubjectPayloadDto(
            Year.of(2024),
            Semester.FIRST,
            "s11001",
            "30109"
        )

        Assertions.assertThat(comparePayload(targetPayload, dto)).isTrue()
    }
}