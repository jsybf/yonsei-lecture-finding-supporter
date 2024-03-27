package gitp.scrapingbatch.utils

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import gitp.scrapingbatch.dto.response.LectureResponseDto
import gitp.scrapingbatch.dto.response.MileageResponseDto
import gitp.scrapingbatch.request.objectmapper.LectureResponseDeserializer
import gitp.scrapingbatch.request.objectmapper.MileageResponseObjectMapper

object MyUtils {
    fun getCommonObjectMapper(): ObjectMapper {
        return ObjectMapper()
            .registerKotlinModule()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(
                SimpleModule()
                    .addDeserializer(
                        LectureResponseDto::class.java,
                        LectureResponseDeserializer()
                    )
                    .addDeserializer(
                        MileageResponseDto::class.java,
                        MileageResponseObjectMapper()
                    )
            )
    }
}