package gitp.scrapingbatch.repository

import gitp.entity.DptGroup
import org.springframework.data.jpa.repository.JpaRepository

interface DptGroupRepository : JpaRepository<DptGroup, Long> {
}