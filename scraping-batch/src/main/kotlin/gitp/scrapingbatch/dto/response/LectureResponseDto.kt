package gitp.scrapingbatch.dto.response

import gitp.scrapingbatch.dto.response.location.PeriodAndLocationDto

data class LectureResponseDto(
    val name: String,
    val professor: ProfessorDto,
    val lectureId: LectureIdDto,
    val periodAndLocationDtoList: List<PeriodAndLocationDto>?
)
