package gitp.scrapingbatch.request

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.module.kotlin.readValue
import gitp.scrapingbatch.dto.response.DeserializableMarker
import java.util.*

class YonseiObjectProducer<T : DeserializableMarker>(
    private val jsonQueue: Queue<JsonNode>,
    private val typeReference: TypeReference<T>,
    private val objectMapper: ObjectMapper

) {
    companion object {
        inline fun <reified K : DeserializableMarker> of(
            jsonArray: ArrayNode,
            objectMapper: ObjectMapper
        ): YonseiObjectProducer<K> {
            return YonseiObjectProducer(
                LinkedList<JsonNode>(jsonArray.toList()) as Queue<JsonNode>,
                object : TypeReference<K>() {},
                objectMapper
            )
        }
    }

    fun pop(): T? {
        val json: JsonNode = jsonQueue.poll() ?: return null
        return objectMapper.readValue(json.toString(), typeReference)
    }
}