package gitp.scrapingbatch.request.dto.location

import gitp.scrapingbatch.request.dto.location.LectureLocationDto
import gitp.type.Day

data class PeriodAndLocationDto(
    val locationDto: LectureLocationDto,
    val day: Day,
    val periods: Set<Int>
)
