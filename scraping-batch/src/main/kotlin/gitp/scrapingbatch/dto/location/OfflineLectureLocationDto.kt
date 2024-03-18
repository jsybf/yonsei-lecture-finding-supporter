package gitp.scrapingbatch.dto.location

import gitp.type.YonseiBuilding

data class OfflineLectureLocationDto(
    val building: YonseiBuilding,
    val address: String?
) : LectureLocationDto
