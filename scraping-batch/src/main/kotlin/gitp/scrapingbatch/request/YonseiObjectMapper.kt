package gitp.scrapingbatch.request

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import gitp.scrapingbatch.dto.response.DeserializableMarker
import gitp.scrapingbatch.exception.ResolutionException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

/**
 * @param postDeserialize: using when manipulating json required before object mapping
 * @sample
 * {
 *      "foo" : [
 *          {
 *              some key-values
 *          },
 *          {
 *              some key-values
 *          }
 *      ]
 * }
 * wanting list of object but if received json formed like above
 * manipulating like below required
 *      [
 *          {
 *              some key-values
 *          },
 *          {
 *              some key-values
 *          }
 *      ]
 */
class YonseiObjectMapper<T : DeserializableMarker>(
    private val typeReference: TypeReference<T>,
    private val listTypeReference: TypeReference<List<T>>,
    private val postDeserialize: ((JsonNode) -> JsonNode)?,
    private val objectMapper: ObjectMapper,
    private val skipPredicate: Map<String, String> = emptyMap()
) {
    private val log: Logger = LoggerFactory.getLogger(YonseiObjectMapper::class.java)

    companion object {
        inline fun <reified K : DeserializableMarker> of(
            objectMapper: ObjectMapper,
            skipPredicate: Map<String, String> = emptyMap(),
            noinline postDeserialize: ((JsonNode) -> JsonNode)?,
        ): YonseiObjectMapper<K> {
            return YonseiObjectMapper(
                object : TypeReference<K>() {},
                object : TypeReference<List<K>>() {},
                postDeserialize,
                objectMapper,
                skipPredicate
            )
        }
    }

    /**
     * TODO: 오류 수정
     * refineJson이 json array가 올것을 가정하고 filtering을 하고 있다.
     * 한개의 json이 refineJson으로 들어갈때는 첫번째 필드만 리턴해보린다.
     */
    fun refineJson(rawJson: String): ArrayNode {
        val json: JsonNode = objectMapper.readTree(rawJson)
        return (postDeserialize?.invoke(json) ?: json)
            .filter { jsonNode: JsonNode ->
                skipPredicate
                    .map { jsonNode.get(it.key).asText() == it.value }
                    .all { it == false }
            }
            .let { objectMapper.valueToTree(it) }
    }

    fun map(json: String): T {
        try {
            return objectMapper.readValue(
                refineJson(json).toString(),
                listTypeReference
            )[0]
        } catch (e: ResolutionException) {
            e.rawResponseJson = json
            throw e
        }
    }

    fun mapList(json: String): List<T> {
        try {
            return objectMapper.readValue(
                refineJson(json).toString(),
                listTypeReference
            )
        } catch (e: ResolutionException) {
            e.rawResponseJson = json
            throw e
        }

    }

    fun getObjectProducer(json: String): YonseiObjectProducer<out DeserializableMarker> {
        return YonseiObjectProducer(
            LinkedList<JsonNode>(refineJson(json).toList()) as Queue<JsonNode>,
            typeReference,
            objectMapper.copy()
        )
    }

}