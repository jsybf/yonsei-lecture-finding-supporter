package gitp.scrapingbatch.request.objectmapper

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import gitp.scrapingbatch.dto.response.LectureIdDto
import gitp.scrapingbatch.dto.response.LectureResponseDto
import gitp.scrapingbatch.dto.response.ProfessorDto
import gitp.scrapingbatch.dto.response.SimpleLectureResponseDto
import gitp.scrapingbatch.request.objectmapper.resolver.Resolvers

class SimpleLectureDeserializer :
    StdDeserializer<SimpleLectureResponseDto>(SimpleLectureResponseDto::class.java) {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): SimpleLectureResponseDto{
        val jsonTree: JsonNode = p!!.codec.readTree<JsonNode>(p)

        val lectureIdList: List<String> = jsonTree
            .get("subjtnbCorsePrcts")
            .asText()
            .split("-")
            .takeIf { it.size == 3 }
            ?: throw IllegalStateException("""lecture id's form should be "0000000"-"00"-"00" """)

        return SimpleLectureResponseDto(
            jsonTree.get("subjtNm").asText(),
            jsonTree.get("cgprfNm").asText()
                .split(",")
                .map {
                    ProfessorDto(
                        null,
                        it
                    )
                }.toList(),
            LectureIdDto(
                null,
                lectureIdList[0],
                lectureIdList[1],
                lectureIdList[2]
            )
        )
    }
}