package gitp.scrapingbatch.repository

import gitp.entity.OfflineLectureLocation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface OfflineLectureLocationRepository : JpaRepository<OfflineLectureLocation, Long?> {

    @Query(
        """
        SELECT COUNT(offll) > 0
        FROM OfflineLectureLocation offll
        WHERE 
            offll.building = :#{#offlineLectureLocation.building} AND 
            offll.address = :#{#offlineLectureLocation.address}
    """
    )
    fun existsByContent(
        @Param("offlineLectureLocation") offlineLectureLocation:
        OfflineLectureLocation
    ): Boolean

    @Query(
        """
        SELECT offll
        FROM OfflineLectureLocation offll
        WHERE 
            offll.building = :#{#offlineLectureLocation.building} AND 
            offll.address = :#{#offlineLectureLocation.address}
    """
    )
    fun findByContent(
        @Param("offlineLectureLocation") offlineLectureLocation:
        OfflineLectureLocation
    ): OfflineLectureLocation?


}