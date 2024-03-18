package gitp.scrapingbatch.repository

import gitp.entity.LectureId
import org.springframework.data.jpa.repository.JpaRepository

interface LectureIdRepository : JpaRepository<LectureId, Long?> {
}