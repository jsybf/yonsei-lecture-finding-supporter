package gitp.scrapingbatch.repository

import gitp.entity.Professor
import org.springframework.data.jpa.repository.JpaRepository

interface ProfessorRepository : JpaRepository<Professor, Long?> {
    fun existsByName(name: String): Boolean
    fun findByName(name: String): Professor?
}