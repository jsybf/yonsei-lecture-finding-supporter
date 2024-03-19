package gitp.scrapingbatch.repository

import gitp.entity.LectureId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface LectureIdRepository : JpaRepository<LectureId, Long?> {

    // retrieve all rows can cause performance issue
    // but jpql doesn't support limits
    @Query(
        """
        SELECT COUNT(li) > 0
        FROM LectureId li
        WHERE 
            li.mainId = :#{#lectureId.mainId} AND 
            li.classDivisionId = :#{#lectureId.classDivisionId} AND 
            li.subId = :#{#lectureId.subId}
    """
    )
    fun existsByContent(@Param("lectureId") lectureId: LectureId): Boolean

    @Query(
        """
        SELECT li
        FROM LectureId li
        WHERE 
            li.mainId = :#{#lectureId.mainId} AND 
            li.classDivisionId = :#{#lectureId.classDivisionId} AND 
            li.subId = :#{#lectureId.subId}
    """
    )
    fun findByContent(@Param("lectureId") lectureId: LectureId): LectureId?
}