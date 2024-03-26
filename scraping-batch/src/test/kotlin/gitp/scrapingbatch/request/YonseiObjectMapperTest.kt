package gitp.scrapingbatch.request

import gitp.scrapingbatch.dto.response.LectureResponseDto
import gitp.scrapingbatch.utils.MyUtils
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class YonseiObjectMapperTest {

    @Test
    fun skip_predicate_test() {

        val objectMapper = YonseiObjectMapper.of<LectureResponseDto>(
            MyUtils.getCommonObjectMapper(),
            mapOf("rmvlcYnNm" to "폐강"),
            null
        )

        Assertions.assertThat(
            objectMapper.refineJson(TestSampleContainer.lectureResponseContainsClosedClasses)
        ).hasSize(1)

    }
}