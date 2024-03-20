package gitp.entity

import gitp.type.Day
import jakarta.persistence.*

@Entity
class LectureTime(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long?,

    @Enumerated(value = EnumType.STRING)
    val day: Day,

    val startPeriod: Int,

    val endPeriod: Int,

    @ManyToOne
    val lectureLocation: LectureLocation,

    // TODO: remove nullable
    @ManyToOne
    val lecture: Lecture?
) {
}