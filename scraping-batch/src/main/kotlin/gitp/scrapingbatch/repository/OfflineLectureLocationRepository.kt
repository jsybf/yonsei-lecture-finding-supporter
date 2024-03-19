package gitp.scrapingbatch.repository

import gitp.entity.OfflineLectureLocation
import org.springframework.data.jpa.repository.JpaRepository

interface OfflineLectureLocationRepository : JpaRepository<OfflineLectureLocation, Long?> {

}