package gitp.scrapingbatch.service

import gitp.scrapingbatch.repository.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Service

@Service
class RequestPersistService(
    val lectureIdRepository: LectureIdRepository,
    val lectureLocationRepository: LectureLocationRepository,
    val lectureRepository: LectureRepository,
    val lectureTimeRepository: LectureTimeRepository,
    val professorRepository: ProfessorRepository
) {
}