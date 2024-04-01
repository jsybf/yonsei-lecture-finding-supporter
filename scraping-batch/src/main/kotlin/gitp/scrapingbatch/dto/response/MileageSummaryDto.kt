package gitp.scrapingbatch.dto.response

import gitp.entity.Lecture
import gitp.entity.MileageSummary

data class MileageSummaryDto(
    val totalLimit: Int,
    val limitPerGrade: List<Int>,
    val candidate: Int
) : DeserializableMarker {
    init {
        require(limitPerGrade.size == 6)
    }

    fun toEntity(lecture: Lecture): MileageSummary {
        return MileageSummary(
            null,
            lecture,
            totalLimit,
            candidate,
            limitPerGrade[0],
            limitPerGrade[1],
            limitPerGrade[2],
            limitPerGrade[3],
            limitPerGrade[4],
            limitPerGrade[5],
        )
    }
}
