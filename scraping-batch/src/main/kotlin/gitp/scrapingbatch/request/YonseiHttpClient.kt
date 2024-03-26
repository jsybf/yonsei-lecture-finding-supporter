package gitp.scrapingbatch.request

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import gitp.scrapingbatch.dto.response.DeserializableMarker
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandlers

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
class YonseiHttpClient<T : DeserializableMarker>(
//     using TypeReference<T> instead of Klass for Class because of
//     type erasing when using Collection<T> (ex: List<fooDto>)
    private val typeReference: TypeReference<T>,
    private val url: String,
    private val postDeserialize: ((JsonNode) -> JsonNode)?,
    private val objectMapper: ObjectMapper,
    private val skipPredicate: Map<String, String> = emptyMap()
) {
    private val httpClient: HttpClient = HttpClient.newHttpClient()
    private val log: Logger = LoggerFactory.getLogger(YonseiHttpClient::class.java)

    companion object {
        inline fun <reified K : DeserializableMarker> of(
            url: String,
            objectMapper: ObjectMapper,
            skipPredicate: Map<String, String> = emptyMap(),
            noinline postDeserialize: ((JsonNode) -> JsonNode)?,
        ): YonseiHttpClient<K> {
            return YonseiHttpClient(
                object : TypeReference<K>() {},
                url,
                postDeserialize,
                objectMapper,
                skipPredicate
            )
        }
    }

    fun retrieveAndMap(payloads: String): T {
        val response: String = retrieve(payloads)

        return objectMapper.readValue(
            refineJson(objectMapper.readTree(response)).toString()
            , typeReference)
    }

    fun refineJson(json: JsonNode):  ArrayNode {
            return (postDeserialize?.invoke(json) ?: json)
                .filter { jsonNode: JsonNode ->
                    skipPredicate
                        .map { jsonNode.get(it.key).asText() == it.value }
                        .all { it == false }
                }
                .let { objectMapper.valueToTree(it) }
    }

    fun retrieve(payloads: String): String {
        val request: HttpRequest = HttpRequest.newBuilder()
            .uri(URI(url))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .POST(HttpRequest.BodyPublishers.ofString(payloads))
            .build()

        val response: HttpResponse<String> = httpClient.send(request, BodyHandlers.ofString())

        if (response.statusCode() != 200) {
            log.warn(
                """
                status-code: ${response.statusCode()}
                payloads: $payloads
                url: ${response.uri()}
            """.trimIndent()
            )
        }

        return response.body()
    }

}