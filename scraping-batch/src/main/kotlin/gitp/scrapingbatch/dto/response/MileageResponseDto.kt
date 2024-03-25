package gitp.scrapingbatch.dto.response

import gitp.type.MajorType

data class MileageResponseDto(
    val rank: Int,
    val mileage: Int,
    val ifSucceed: Boolean,
    val grade: Int,
    val major: MajorType,
    val totalCreditRatio: Pair<Int, Int>,
    val lastSemesterCreditRatio: Double,
    val appliedLectureNum: Int,
    val ifFirstRegister: Boolean,
    val ifGraduateApplied: Boolean,
    // val ifDisabled: Boolean

)
