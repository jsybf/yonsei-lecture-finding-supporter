package gitp.scrapingbatch.dto

import gitp.scrapingbatch.dto.location.PeriodAndLocationDto

data class LectureResponseDto(
    val name: String,
    val professor: ProfessorDto,
    val lectureId: LectureIdDto,
    val periodAndLocationDtoList: List<PeriodAndLocationDto>?
)
