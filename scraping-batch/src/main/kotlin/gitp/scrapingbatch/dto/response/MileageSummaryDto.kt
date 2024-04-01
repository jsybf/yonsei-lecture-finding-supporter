package gitp.scrapingbatch.dto.response

data class MileageSummaryDto(
    val totalLimit: Int,
    val limitPerGrade: List<Int>,
    val candidate: Int
):DeserializableMarker {
    init {
        require(limitPerGrade.size == 6)
    }
}
