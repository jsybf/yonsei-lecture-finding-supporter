package gitp.entity

import jakarta.persistence.*

@Entity
class MileageSummary(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long?,


    @OneToOne
    val lecture: Lecture,
    val totalLimit: Int,
    val candidate: Int,

    private var grade1Limit: Int,
    private var grade2Limit: Int,
    private var grade3Limit: Int,
    private var grade4Limit: Int,
    private var grade5Limit: Int,
    private var grade6Limit: Int,
) {
    fun setLimitOfGrade(grade: Int, limit: Int) {
        return when (grade) {
            1 -> grade1Limit = limit
            2 -> grade2Limit = limit
            3 -> grade3Limit = limit
            4 -> grade4Limit = limit
            5 -> grade5Limit = limit
            6 -> grade6Limit = limit
            else -> throw IllegalArgumentException("grade should be in range 1 <= grade <= 6")
        }
    }

    fun getLimitOfGrade(grade: Int): Int {
        return when (grade) {
            1 -> grade1Limit
            2 -> grade2Limit
            3 -> grade3Limit
            4 -> grade4Limit
            5 -> grade5Limit
            6 -> grade6Limit
            else -> throw IllegalArgumentException("grade should be in range 1 <= grade <= 6")
        }
    }
}