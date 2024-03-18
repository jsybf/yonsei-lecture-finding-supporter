package gitp.entity

import jakarta.persistence.*

@Entity
class Lecture(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long?,

    @Column(length = 60)
    val name: String?,

    @ManyToOne
    val professor: Professor,

    @ManyToOne
    val lectureId: LectureId

) {
}