package gitp.scrapingbatch.dto.response.location

import gitp.type.OnlineLectureType

data class OnlineLectureLocationDto(
    val type: OnlineLectureType,
    val overlapAllowed: Boolean = true
) : LectureLocationDto