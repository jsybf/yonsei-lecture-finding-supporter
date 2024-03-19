package gitp.scrapingbatch.dto.response.location

import gitp.entity.OfflineLectureLocation
import gitp.type.YonseiBuilding

data class OfflineLectureLocationDto(
    val building: YonseiBuilding,
    val address: String?
) : LectureLocationDto {
    fun toEntity(): OfflineLectureLocation {
        return OfflineLectureLocation(
            building,
            address
        )
    }
}
