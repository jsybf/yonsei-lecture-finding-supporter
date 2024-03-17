package gitp.scrapingbatch.request.payload.resolver.dto

import gitp.scrapingbatch.request.payload.resolver.type.YonseiBuilding

data class OfflineLectureLocationDto(
    val building: YonseiBuilding,
    val address: String?
) : LectureLocationDto
