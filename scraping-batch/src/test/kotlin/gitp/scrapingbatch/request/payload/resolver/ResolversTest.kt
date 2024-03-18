package gitp.scrapingbatch.request.payload.resolver

import gitp.scrapingbatch.request.objectmapper.resolver.Resolvers
import gitp.type.Day
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class ResolversTest {
    @Test
    fun test_with_sample_data() {
        val testString1: String = "월1,2/수3/(목3)"
        val testString2: String = "월1,2/수3/(월3)"
        val testString3: String = "목1,2(금3,4)"
        assertThat(Resolvers.resolvePeriod(testString1))
            .containsOnly(
                Pair(Day.MON, setOf(1, 2)),
                Pair(Day.WEN, setOf(3)),
                Pair(Day.THU, setOf(3))
            )
        assertThat(Resolvers.resolvePeriod(testString2))
            .containsOnly(
                Pair(Day.MON, setOf(1, 2)),
                Pair(Day.WEN, setOf(3)),
                Pair(Day.MON, setOf(3)),
            )
        assertThat(Resolvers.resolvePeriod(testString3))
            .containsOnly(
                Pair(Day.THU, setOf(1, 2)),
                Pair(Day.FRI, setOf(3, 4))
            )
    }

    @Test
    fun throw_exception_when_unexpected_day_symbol() {
        val testString1: String = "욜1,2/수3/(목3)"

        assertThatThrownBy { Resolvers.resolvePeriod(testString1) }
            .isInstanceOf(IllegalStateException::class.java)
    }
}