package gitp.scrapingbatch.dto.response

import gitp.scrapingbatch.dto.response.location.PeriodAndLocationDto

data class LectureResponseDto(
    val name: String,
    val professor: List<ProfessorDto>,
    val lectureId: LectureIdDto,
    val periodAndLocationDtoList: List<PeriodAndLocationDto>?
) : DeserializableMarker
