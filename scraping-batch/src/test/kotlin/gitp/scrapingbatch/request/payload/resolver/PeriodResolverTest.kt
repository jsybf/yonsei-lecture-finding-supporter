package gitp.scrapingbatch.request.payload.resolver

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class PeriodResolverTest {
    @Test
    fun test_with_sample_data() {
        val testString1: String = "월1,2/수3/(목3)"
        val testString2: String = "월1,2/수3/(월3)"
        val testString3: String = "목1,2(금3,4)"
        assertThat(PeriodResolver.resolve(testString1))
            .containsOnly(
                entry(Day.MON, setOf(1, 2)),
                entry(Day.WEN, setOf(3)),
                entry(Day.THU, setOf(3))
            )
        assertThat(PeriodResolver.resolve(testString2))
            .containsOnly(
                entry(Day.MON, setOf(1, 2, 3)),
                entry(Day.WEN, setOf(3))
            )
        assertThat(PeriodResolver.resolve(testString3))
            .containsOnly(
                entry(Day.THU, setOf(1, 2)),
                entry(Day.FRI, setOf(3, 4))
            )
    }

    @Test
    fun throw_exception_when_unexpected_day_symbol() {
        val testString1: String = "욜1,2/수3/(목3)"

        assertThatThrownBy { PeriodResolver.resolve(testString1) }
            .isInstanceOf(IllegalStateException::class.java)
    }
}