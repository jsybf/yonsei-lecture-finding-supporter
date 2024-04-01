package gitp.scrapingbatch.request.objectmapper

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import gitp.scrapingbatch.dto.response.MileageResponseDto
import gitp.scrapingbatch.request.objectmapper.resolver.Resolvers

class MileageResponseObjectMapper :
    StdDeserializer<MileageResponseDto>(MileageResponseDto::class.java) {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): MileageResponseDto {
        val jsonTree: JsonNode = p!!.codec.readTree<JsonNode>(p)

        return MileageResponseDto(
            jsonTree.get("mlgRank").asInt(),
            jsonTree.get("mlgVal").asInt(),
            jsonTree.get("mlgAppcsPrcesDivNm").asText()?.matches(Regex("Y")) ?: false,
            jsonTree.get("hy").asInt(),
            Resolvers.resolveMajor(jsonTree.get("mjsbjYn").asText()),
            Resolvers.resolveTotalCreditRatio(
                jsonTree.get("ttCmpsjGrdtnCmpsjCdt").asText()
            ),
            jsonTree.get("jstbfSmtCmpsjCdtRto").asDouble(),
            jsonTree.get("aplySubjcCnt").asInt(),
            jsonTree.get("fratlcYn").asText().matches(Regex("Y")),
            jsonTree.get("grdtnAplyYn").asText().matches(Regex("Y")),
        )
    }
}