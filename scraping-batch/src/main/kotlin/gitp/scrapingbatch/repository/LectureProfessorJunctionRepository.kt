package gitp.scrapingbatch.repository

import gitp.entity.LectureProfessorJunction
import org.springframework.data.jpa.repository.JpaRepository

interface LectureProfessorJunctionRepository : JpaRepository<LectureProfessorJunction, Long> {
}