package gitp.scrapingbatch.dto.payload

import gitp.type.Semester
import java.time.Year

class LecturePayloadDto(
    val year: Year,
    val semester: Semester,
    val dptGroupId: String,
    val dptId: String
) : PayloadDto {
    override fun toMap(): Map<String, String> {
        return mapOf(
            "%40d1%23syy" to year.toString(),
            "%40d1%23smtDivCd" to semester.code.toString(),
            "%40d1%23univCd" to dptGroupId,
            "%40d1%23faclyCd" to dptId
        )
    }
}