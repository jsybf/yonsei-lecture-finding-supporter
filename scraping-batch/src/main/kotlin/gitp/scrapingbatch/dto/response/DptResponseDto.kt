package gitp.scrapingbatch.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

data class DptResponseDto(
    @JsonProperty(value = "deptNm")
    val dptName: String,
    @JsonProperty(value = "deptCd")
    val dptId: String,
) : DeserializableMarker
