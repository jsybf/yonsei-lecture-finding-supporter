package gitp.scrapingbatch.dto.response

data class SimpleLectureResponseDto(
    val name: String,
    val professor: List<ProfessorDto>,
    val lectureId: LectureIdDto,
) : DeserializableMarker
