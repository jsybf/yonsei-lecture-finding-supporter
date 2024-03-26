package gitp.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne

@Entity
class LectureProfessorJunction(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long?,

    @ManyToOne
    val lecture: Lecture,

    @ManyToOne
    val professor: Professor
) {
}