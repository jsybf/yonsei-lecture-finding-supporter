package gitp.scrapingbatch.request.dto.location

import gitp.scrapingbatch.request.dto.location.LectureLocationDto
import gitp.type.YonseiBuilding

data class OfflineLectureLocationDto(
    val building: YonseiBuilding,
    val address: String?
) : LectureLocationDto
