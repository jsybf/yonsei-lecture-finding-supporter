package gitp.entity

import jakarta.persistence.*

@Entity
class Lecture(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long?,

    @Column(length = 100)
    val name: String,

    @ManyToOne
    val lectureId: LectureId

) {
}