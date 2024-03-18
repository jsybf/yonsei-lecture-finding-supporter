package gitp.scrapingbatch.request.dto

import gitp.type.YonseiBuilding

data class OfflineLectureLocationDto(
    val building: YonseiBuilding,
    val address: String?
) : LectureLocationDto
