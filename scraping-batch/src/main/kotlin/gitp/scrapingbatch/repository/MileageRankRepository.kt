package gitp.scrapingbatch.repository

import gitp.entity.MileageRank
import org.springframework.data.jpa.repository.JpaRepository

interface MileageRankRepository : JpaRepository<MileageRank, Long> {
}