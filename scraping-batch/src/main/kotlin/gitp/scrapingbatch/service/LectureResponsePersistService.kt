package gitp.scrapingbatch.service

import gitp.entity.Lecture
import gitp.entity.LectureTime
import gitp.entity.OfflineLectureLocation
import gitp.entity.OnlineLectureLocation
import gitp.scrapingbatch.dto.response.LectureResponseDto
import gitp.scrapingbatch.dto.response.location.OfflineLectureLocationDto
import gitp.scrapingbatch.dto.response.location.OnlineLectureLocationDto
import gitp.scrapingbatch.repository.*
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
@Transactional
class LectureResponsePersistService(
    val lectureIdRepository: LectureIdRepository,
    val offlineLectureLocationRepository: OfflineLectureLocationRepository,
    val onlineLectureLocationRepository: OnlineLectureLocationRepository,
    val lectureRepository: LectureRepository,
    val lectureTimeRepository: LectureTimeRepository,
    val professorRepository: ProfessorRepository
) {
    fun save(responseDto: LectureResponseDto) {
        if (!professorRepository.existsByName(responseDto.professor.name)) {
            professorRepository.save(responseDto.professor.toEntity())
        }

        if (!lectureIdRepository.existsByContent(responseDto.lectureId.toEntity())) {
            lectureIdRepository.save(responseDto.lectureId.toEntity())
        }

        val lectureEntityId: Long = lectureRepository.save(
            Lecture(
                null,
                responseDto.name,
                professorRepository.findByName(responseDto.professor.name)!!,
                lectureIdRepository.findByContent(responseDto.lectureId.toEntity())!!
            )
        ).id!!


        for (periodAndLocationDto in responseDto.periodAndLocationDtoList!!) {
            when (periodAndLocationDto.locationDto) {

                is OfflineLectureLocationDto -> {
                    val entity: OfflineLectureLocation =
                        periodAndLocationDto.locationDto.toEntity()

                    if (!offlineLectureLocationRepository.existsByContent(entity)) {
                        offlineLectureLocationRepository.save(entity)
                    }
                }

                is OnlineLectureLocationDto -> {
                    val entity: OnlineLectureLocation =
                        periodAndLocationDto.locationDto.toEntity()

                    if (!onlineLectureLocationRepository.existsByContent(entity)) {
                        onlineLectureLocationRepository.save(entity)
                    }
                }

            } // when scope end
        }// for scope end


        for (periodAndLocationDto in responseDto.periodAndLocationDtoList) {
            lectureTimeRepository.save(
                LectureTime(
                    null,
                    periodAndLocationDto.day,
                    periodAndLocationDto.periods.min(),
                    periodAndLocationDto.periods.max(),
                    when (periodAndLocationDto.locationDto) {
                        is OfflineLectureLocationDto -> {
                            offlineLectureLocationRepository.findByContent(
                                periodAndLocationDto
                                    .locationDto.toEntity()
                            )!!
                        }

                        is OnlineLectureLocationDto -> {
                            onlineLectureLocationRepository.findByContent(
                                periodAndLocationDto
                                    .locationDto.toEntity()
                            )!!
                        }

                        else -> {
                            throw IllegalStateException(
                                "unexpected type ${periodAndLocationDto.locationDto::class}"
                            )
                        }
                    },
                    lectureRepository.findByIdOrNull(lectureEntityId)!!
                )
            )
        }

    }
}