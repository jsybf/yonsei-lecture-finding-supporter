package gitp.scrapingbatch.repository

import gitp.entity.DptGroup
import gitp.entity.MileageSummary
import org.springframework.data.jpa.repository.JpaRepository

interface MileageSummaryRepository : JpaRepository<MileageSummary, Long> {
}