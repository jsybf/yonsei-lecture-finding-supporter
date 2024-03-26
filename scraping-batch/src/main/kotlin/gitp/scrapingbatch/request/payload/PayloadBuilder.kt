package gitp.yonseiprotohttp.payload

import gitp.scrapingbatch.dto.payload.DptGroupPayloadDto
import gitp.scrapingbatch.dto.payload.DptPayloadDto
import gitp.scrapingbatch.dto.payload.LecturePayloadDto
import gitp.scrapingbatch.dto.payload.PayloadDto

object PayloadBuilder {

    fun toPayload(dto: PayloadDto): String {
        val defaultPayload: Map<String, String> = when (dto) {
            is DptGroupPayloadDto -> DefaultPayloadContainer.dptGroupPayload
            is DptPayloadDto -> DefaultPayloadContainer.dtpPayload
            is LecturePayloadDto -> DefaultPayloadContainer.lecturePayload
            else -> {
                throw IllegalArgumentException("type of dto: ${dto::class}")
            }
        }

        val payloadMap: Map<String, String> =
            defaultPayload + dto.toMap() + DefaultPayloadContainer.commonPayload

        return payloadMap
            .map { "${it.key}=${it.value}" }
            .reduce { acc: String, s: String -> "$acc&$s" }
    }
}