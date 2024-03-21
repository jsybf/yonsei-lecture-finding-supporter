package gitp.scrapingbatch.repository

import gitp.entity.Dpt
import org.springframework.data.jpa.repository.JpaRepository

interface DptRepository : JpaRepository<Dpt, Long> {
}