package gitp.scrapingbatch.dto.payload

import gitp.scrapingbatch.dto.response.LectureIdDto
import gitp.type.Semester
import java.time.Year

data class MileageSummaryPayloadDto(
    val year: Year,
    val semester: Semester,
    val lectureId: LectureIdDto
) : PayloadDto {
    override fun toMap(): Map<String, String> {
        return mapOf(
            "%40d1%23syy" to year.toString(),
            "%40d1%23smtDivCd" to semester.code.toString(),
            "%40d1%23subjtnb" to lectureId.mainId,
            "%40d1%23corseDvclsNo" to lectureId.classDivisionId,
            "%40d1%23prctsCorseDvclsNo" to lectureId.subId,
            "%40d1%23syySmtDivCd" to year.toString() + semester.code.toString(),
        )
    }
}

