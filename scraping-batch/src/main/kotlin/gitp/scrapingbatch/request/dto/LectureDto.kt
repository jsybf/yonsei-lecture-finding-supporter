package gitp.scrapingbatch.request.dto

import gitp.entity.LectureId

data class LectureDto(
    val id: Long?,
    val name: String,
    val professor: ProfessorDto,
    val lectureId: LectureIdDto,
    val periodAndLocationDtoList: List<PeriodAndLocationDto>?
)
