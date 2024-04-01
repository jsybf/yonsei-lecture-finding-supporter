package gitp.entity

import gitp.type.Fraction
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
    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "denominator", column = Column(name = "totalCredit")),
        AttributeOverride(name = "numerator", column = Column(name = "currentCredit"))
    )
    val totalCreditRatio: Fraction,
    @Enumerated(value = EnumType.STRING)
    val major: MajorType,
    val lastSemesterCreditRatio: Double,
    val appliedLectureNum: Int,
    val ifFirstRegister: Boolean,
    val ifGraduateApplied: Boolean,

    )