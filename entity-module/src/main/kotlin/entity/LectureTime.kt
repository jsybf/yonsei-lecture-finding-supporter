package gitp.entity

import jakarta.persistence.*

@Entity
class LectureTime(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long?,

    @ManyToOne
    val lectureLocation: LectureLocation,

    @ManyToOne
    val lectureTime: LectureTime?
) {
}