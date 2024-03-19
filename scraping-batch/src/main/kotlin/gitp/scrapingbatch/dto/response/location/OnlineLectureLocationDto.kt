package gitp.scrapingbatch.dto.response.location

import gitp.entity.OnlineLectureLocation
import gitp.type.OnlineLectureType

data class OnlineLectureLocationDto(
    val type: OnlineLectureType,
    val overlapAllowed: Boolean = true
) : LectureLocationDto {
    fun toEntity(): OnlineLectureLocation {
        return OnlineLectureLocation(
            type,
            overlapAllowed
        )
    }
}