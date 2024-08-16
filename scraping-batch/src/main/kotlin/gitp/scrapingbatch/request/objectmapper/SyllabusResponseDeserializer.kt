package gitp.scrapingbatch.request.objectmapper

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import gitp.scrapingbatch.dto.response.*
import gitp.scrapingbatch.request.objectmapper.resolver.Resolvers

class SyllabusResponseDeserializer :
    StdDeserializer<SyllabusResponseDto>(SyllabusResponseDto::class.java) {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): SyllabusResponseDto{
        val jsonTree: JsonNode = p!!.codec.readTree<JsonNode>(p)

        return SyllabusResponseDto(
            jsonTree.asText()
        )
    }
}