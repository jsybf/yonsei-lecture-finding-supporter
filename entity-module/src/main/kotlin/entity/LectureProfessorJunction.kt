package gitp.entity

import jakarta.persistence.*

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