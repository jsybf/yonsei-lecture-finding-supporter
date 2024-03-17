package gitp.scrapingbatch.request.payload.resolver

import gitp.scrapingbatch.request.payload.resolver.type.Day

/**
 * form of period in json response has some forms
 * form1: 월1,2(수3,4)
 * form2: 월1,2/목2
 * form3: 월1,2/(목2)/금3
 */
object PeriodResolver {
    fun resolve(raw: String): Map<Day, Set<Int>> {
        val periodGroupExtractor: Regex = Regex("""[가-힣]([0-9]+,?)+""")
        val dayExtractor: Regex = Regex("""[월화수목금토일]""")
        val periodExtractor: Regex = Regex("""[0-9]+""")

        val periodGroupList = periodGroupExtractor.findAll(raw).map { it.value }.toList()

        val periodMap: MutableMap<Day, MutableSet<Int>> = mutableMapOf()

        for (refinedPeriod in periodGroupList) {
            val day: Day = Day.of(
                dayExtractor
                    .find(refinedPeriod)
                    ?.value
                    ?: throw IllegalStateException(
                        "unexpected day symbol. input: [$raw] only 월화수목금토일 is allowed"
                    )
            )
            val periodSet: MutableSet<Int> = periodExtractor
                .findAll(refinedPeriod)
                .map { it.value.toInt() }
                .toMutableSet()

            if (!periodMap.containsKey(day)) {
                periodMap[day] = periodSet
            } else {
                periodMap[day]!!.addAll(periodSet)
            }
        }

        return periodMap
    }

}