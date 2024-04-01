package gitp.scrapingbatch.request.objectmapper

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import gitp.scrapingbatch.dto.response.MileageResponseDto
import gitp.scrapingbatch.dto.response.MileageSummaryDto

class MileageSummaryDeserializer
    : StdDeserializer<MileageSummaryDto>(MileageResponseDto::class.java) {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): MileageSummaryDto {
        val jsonTree: JsonNode = p!!.codec.readTree(p)

        val limitPerGradeList = List(6) {
            jsonTree.get("sy${it + 1}PercpCnt").asInt()
        }

        return MileageSummaryDto(
            jsonTree.get("atnlcPercpCnt").asInt(),
            limitPerGradeList,
            jsonTree.get("cnt").asInt()
        )
    }
}