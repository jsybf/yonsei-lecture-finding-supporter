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
                listOf(
                    Pair(Day.MON, setOf(1, 2)),
                ),
                listOf(
                    Pair(Day.WEN, setOf(3)),
                ),
                listOf(
                    Pair(Day.THU, setOf(3))
                ),
            )
        assertThat(Resolvers.resolvePeriod(testString2))
            .containsOnly(
                listOf(
                    Pair(Day.MON, setOf(1, 2)),
                ),
                listOf(
                    Pair(Day.WEN, setOf(3)),
                ),
                listOf(
                    Pair(Day.MON, setOf(3)),
                ),
            )
        assertThat(Resolvers.resolvePeriod(testString3))
            .containsOnly(
                listOf(
                    Pair(Day.THU, setOf(1, 2)),
                ),
                listOf(
                    Pair(Day.FRI, setOf(3, 4))
                ),
            )
    }

    // @Test
    // fun throw_exception_when_unexpected_day_symbol() {
    //     val testString1: String = "욜1,2/수3/(목3)"
    //
    //     assertThatThrownBy { Resolvers.resolvePeriod(testString1) }
    //         .isInstanceOf(IllegalStateException::class.java)
    // }

    @Test
    fun splitPeriod_test() {
        val testSample1: String = "월3,4/수3(수4)"
        val testSample2: String = "화3,4,목3(목4)"
        val testSample3: String = "월3,4,수3/수4"
        val testSample4: String = "화8,9/(금6,7)"

        assertThat(Resolvers.splitPeriod(testSample1)).containsOnly(
            "월3,4", "수3", "수4"
        )
        assertThat(Resolvers.splitPeriod(testSample2)).containsOnly(
            "화3,4,목3", "목4"
        )
        assertThat(Resolvers.splitPeriod(testSample3)).containsOnly(
            "월3,4,수3", "수4"
        )
        assertThat(Resolvers.splitPeriod(testSample4)).containsOnly(
            "화8,9", "금6,7"
        )
    }

    @Test
    fun splitLocation_test() {
        val testSample1: String = "상본B110(상본B110)"
        val testSample2: String = "상본B110/(상본B103)"
        val testSample3: String = "상본B110/(상본B103)/동영상콘텐츠(중복수강불가)"

        assertThat(Resolvers.splitLocation(testSample1)).containsOnly(
            "상본B110",
            "상본B110"
        )
        assertThat(Resolvers.splitLocation(testSample2)).containsOnly(
            "상본B110",
            "상본B103"
        )
        assertThat(Resolvers.splitLocation(testSample3)).containsOnly(
            "상본B110",
            "상본B103",
            "동영상콘텐츠(중복수강불가)"

        )
    }

    @Test
    fun newResolvePeriod_test() {
        val testSample3: String = "월3,4/수3(수4)"
        val testString1: String = "월1,3,목1,2(금3,4)"

        Resolvers.resolvePeriod(testSample3)
            .forEachIndexed { index: Int, pairs: List<Pair<Day, Set<Int>>> ->
                println("[$index]")
                for (pair in pairs) {
                    println(pair)
                }
            }
        Resolvers.resolvePeriod(testString1)
            .forEachIndexed { index: Int, pairs: List<Pair<Day, Set<Int>>> ->
                println("[$index]")
                for (pair in pairs) {
                    println(pair)
                }
            }
    }

    @Test
    fun newLocationResolver_test() {
        val testSample1: String = "상본B110(상본B110)"
        val testSample2: String = "상본B110/(상본B103)"
        val testSample3: String = "상본B110/(상본B103)/동영상콘텐츠(중복수강불가)"

        println(Resolvers.resolveLocation(testSample1))
        println(Resolvers.resolveLocation(testSample2))
        println(Resolvers.resolveLocation(testSample3))
    }

    @Test
    fun newResolvePeriodAndLocation_test() {
        val testString1: String = "월1,3,목1,2(금3,4)"
        val testSample2: String = "상본B110/(상본B103)"

        Resolvers.resolvePeriodAndLocation(
            testString1,
            testSample2
        ).forEach { println(it) }
    }
}