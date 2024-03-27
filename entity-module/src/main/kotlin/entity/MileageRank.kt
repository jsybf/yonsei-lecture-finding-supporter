package gitp.entity

import gitp.type.MajorType
import jakarta.persistence.*

@Entity
class MileageRank(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long?,

    @ManyToOne
    val lecture: Lecture,

    val rank: Int,
    val mileage: Int,
    val ifSucceed: Boolean,
    val grade: Int,
    val totalCreditRatio: Pair<Int, Int>,
    @Enumerated(value = EnumType.STRING)
    val major: MajorType,
    val lastSemesterCreditRatio: Double,
    val appliedLectureNum: Int,
    val ifFirstRegister: Boolean,
    val ifGraduateApplied: Boolean,

    )