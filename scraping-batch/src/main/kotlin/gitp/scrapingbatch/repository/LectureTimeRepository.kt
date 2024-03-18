package gitp.scrapingbatch.repository

import gitp.entity.LectureTime
import org.springframework.data.jpa.repository.JpaRepository

interface LectureTimeRepository : JpaRepository<LectureTime, Long?> {
}