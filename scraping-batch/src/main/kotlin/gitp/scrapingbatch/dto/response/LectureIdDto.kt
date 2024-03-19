package gitp.scrapingbatch.dto.response

import gitp.entity.LectureId

data class LectureIdDto(
    val id: Long?,
    val mainId: String,
    val classDivisionId: String,
    val subId: String
) {
    fun toEntity(): LectureId {
        return LectureId(
            id,
            mainId,
            classDivisionId,
            subId
        )
    }
}
