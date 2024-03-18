package gitp.scrapingbatch.repository

import gitp.entity.Lecture
import org.springframework.data.jpa.repository.JpaRepository

interface LectureRepository : JpaRepository<Lecture, Long?> {
}