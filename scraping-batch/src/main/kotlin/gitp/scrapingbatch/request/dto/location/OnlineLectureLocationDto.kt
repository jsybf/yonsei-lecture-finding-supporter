package gitp.scrapingbatch.request.dto.location

import gitp.scrapingbatch.request.dto.location.LectureLocationDto
import gitp.type.OnlineLectureType

data class OnlineLectureLocationDto(
    val type: OnlineLectureType,
    val overlapAllowed: Boolean = true
) : LectureLocationDto