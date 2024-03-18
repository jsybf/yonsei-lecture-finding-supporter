package gitp.scrapingbatch.request.payload.resolver.dto

import gitp.type.Day

data class PeriodAndLocationDto(
    val locationDto: LectureLocationDto,
    val day: Day,
    val periods: Set<Int>
)
