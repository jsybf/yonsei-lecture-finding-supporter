package gitp.yonseiprotohttp.payload.dto

interface PayloadDto {
    fun toMap(): Map<String, String>
}