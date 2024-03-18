package gitp.scrapingbatch.request.dto

import gitp.type.OnlineLectureType

data class OnlineLectureLocationDto(
    val type: OnlineLectureType,
    val overlapAllowed: Boolean = true
) : LectureLocationDto