package gitp.scrapingbatch.request.payload.resolver.dto

import gitp.scrapingbatch.request.payload.resolver.type.Day

data class PeriodAndLocationDto(
    val locationDto: LectureLocationDto,
    val day: Day,
    val periods: Set<Int>
)
