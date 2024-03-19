package gitp.scrapingbatch.repository

import gitp.entity.OnlineLectureLocation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface OnlineLectureLocationRepository : JpaRepository<OnlineLectureLocation, Long?> {

    @Query("""
        SELECT COUNT(ol) > 0
        FROM OnlineLectureLocation as ol
        WHERE 
            ol.overlapAllowed = :#{#onlineLectureLocation.overlapAllowed} AND 
            ol.type = :#{#onlineLectureLocation.type}
    """)
    fun existsByContent(
        @Param("onlineLectureLocation")
        onlineLectureLocation: OnlineLectureLocation
    ): Boolean

}