package gitp.scrapingbatch.repository

import gitp.entity.ObjectMappingError
import org.springframework.data.jpa.repository.JpaRepository

interface ObjectMappingErrorRepository: JpaRepository<ObjectMappingError, Long> {

}