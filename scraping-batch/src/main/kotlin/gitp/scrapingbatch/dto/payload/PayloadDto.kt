package gitp.scrapingbatch.dto.payload

interface PayloadDto {
    fun toMap(): Map<String, String>
}