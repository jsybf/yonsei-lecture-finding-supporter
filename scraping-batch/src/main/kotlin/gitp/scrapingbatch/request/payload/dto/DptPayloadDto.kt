package gitp.yonseiprotohttp.payload.dto

import gitp.yonseiprotohttp.payload.type.Semester
import java.time.Year

data class DptPayloadDto(
    val departmentGroupId: String,
    val year: Year,
    val semester: Semester
) : PayloadDto {
    override fun toMap(): Map<String, String> {
        val payLoadMap: MutableMap<String, String> = mutableMapOf()
        payLoadMap["%40d1%23lv2"] = departmentGroupId
        payLoadMap["%40d1%23syy"] = year.toString()
        payLoadMap["%40d1%23smtDivCd"] = semester.code.toString()

        return payLoadMap
    }
}