package gitp.scrapingbatch.dto.response

import gitp.entity.Professor

data class ProfessorDto(
    val id: Long?,
    val name: String
) {
    fun toEntity(): Professor {
        return Professor(id, name)
    }
}
