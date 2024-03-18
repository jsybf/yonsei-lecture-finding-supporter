package gitp.scrapingbatch.dto.payload

import gitp.type.Semester
import java.time.Year

data class DptGroupPayloadDto(
    val year: Year,
    val semester: Semester
) : PayloadDto {
    override fun toMap(): Map<String, String> {
        val payLoadMap: MutableMap<String, String> = mutableMapOf()
        payLoadMap["%40d1%23syy"] = year.toString()
        payLoadMap["%40d1%23smtDivCd"] = semester.code.toString()

        return payLoadMap
    }
}