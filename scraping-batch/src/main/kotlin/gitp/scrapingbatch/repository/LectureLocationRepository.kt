package gitp.scrapingbatch.repository

import gitp.entity.LectureLocation
import org.springframework.data.jpa.repository.JpaRepository

interface LectureLocationRepository : JpaRepository<LectureLocation, Long?> {
}