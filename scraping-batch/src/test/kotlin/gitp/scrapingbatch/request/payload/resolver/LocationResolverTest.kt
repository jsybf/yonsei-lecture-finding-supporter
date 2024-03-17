package gitp.scrapingbatch.request.payload.resolver

import gitp.scrapingbatch.request.payload.resolver.dto.OfflineLectureLocationDto
import gitp.scrapingbatch.request.payload.resolver.dto.OnlineLectureLocationDto
import gitp.scrapingbatch.request.payload.resolver.type.OnlineLectureType
import gitp.scrapingbatch.request.payload.resolver.type.YonseiBuilding
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LocationResolverTest {
    @Test
    fun test_with_sample_data() {
        val sample1 = "과B103(과232)"
        val sample2 = "공B604/동영상(중복수강불가)"
        val sample3 = "상본B110"
        val sample4 = "동영상(중복수강불가)/(상본B103)/상본B110"
        val sample5 = "동영상콘텐츠/외01"


        assertThat(LocationResolver.resolve(sample1))
            .hasSize(2)
            .containsSequence(
                OfflineLectureLocationDto(
                    YonseiBuilding.SCIENCE,
                    "B103"
                ),
                OfflineLectureLocationDto(
                    YonseiBuilding.SCIENCE,
                    "232"
                )
            )
        assertThat(LocationResolver.resolve(sample2))
            .hasSize(2)
            .containsSequence(
                OfflineLectureLocationDto(
                    YonseiBuilding.ENGINEERING_B,
                    "604"
                ),
                OnlineLectureLocationDto(
                    OnlineLectureType.VIDEO,
                    false
                )
            )
        assertThat(LocationResolver.resolve(sample3))
            .hasSize(1)
            .containsSequence(
                OfflineLectureLocationDto(
                    YonseiBuilding.ECO_MAIN,
                    "B110"
                ),
            )
        assertThat(LocationResolver.resolve(sample4))
            .hasSize(3)
            .containsSequence(
                OnlineLectureLocationDto(
                    OnlineLectureType.VIDEO,
                    false
                ),
                OfflineLectureLocationDto(
                    YonseiBuilding.ECO_MAIN,
                    "B103"
                ),
                OfflineLectureLocationDto(
                    YonseiBuilding.ECO_MAIN,
                    "B110"
                ),
            )
        assertThat(LocationResolver.resolve(sample5))
            .hasSize(2)
            .containsSequence(
                OnlineLectureLocationDto(
                    OnlineLectureType.VIDEO,
                    true
                ),
                OfflineLectureLocationDto(
                    YonseiBuilding.OESOL,
                    "01"
                )
            )
    }
}