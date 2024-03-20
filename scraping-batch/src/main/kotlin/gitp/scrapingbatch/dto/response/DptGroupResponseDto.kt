package gitp.scrapingbatch.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

data class DptGroupResponseDto(
    @JsonProperty(value = "deptNm")
    val dptGroupName: String,
    @JsonProperty(value = "deptCd")
    val dptGroupId: String
)
