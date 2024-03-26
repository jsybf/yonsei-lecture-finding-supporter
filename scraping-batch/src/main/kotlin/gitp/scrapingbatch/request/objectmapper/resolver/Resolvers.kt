package gitp.scrapingbatch.request.objectmapper.resolver

import gitp.scrapingbatch.dto.response.location.LectureLocationDto
import gitp.scrapingbatch.dto.response.location.OfflineLectureLocationDto
import gitp.scrapingbatch.dto.response.location.OnlineLectureLocationDto
import gitp.scrapingbatch.dto.response.location.PeriodAndLocationDto
import gitp.type.Day
import gitp.type.OnlineLectureType
import gitp.type.YonseiBuilding

object Resolvers {
    fun splitPeriod(raw: String): List<String> =
        raw.split("/")
            .flatMap {
                val match: MatchResult? = Regex("""\(([가-힣0-9,]+)\)""")
                    .find(it)
                return@flatMap if (match == null) listOf(it)
                else listOf(it.substring(0, match.range.first), match.groupValues[1])
            }.filter { it != "" }

    fun splitLocation(raw: String): List<String> =
        raw.split("/")
            .flatMap {
                val match: MatchResult? = Regex("""\(([가-힣0-9A-Z,]+)\)""")
                    .find(it)
                return@flatMap if (match == null) listOf(it)
                else if (match.groupValues[1] == "중복수강불가") listOf(it)
                else listOf(it.substring(0, match.range.first), match.groupValues[1])
            }.filter { it != "" }

    fun resolvePeriodAndLocation(
        rawPeriod: String,
        rawLocation: String
    ): List<PeriodAndLocationDto> {
        val resolvedLocation: List<LectureLocationDto> = resolveLocation(rawLocation)
        val resolvedPeriod: List<List<Pair<Day, Set<Int>>>> = resolvePeriod(rawPeriod)

        if (resolvedLocation.size != resolvedPeriod.size) {
            if (resolvedLocation.distinct().size == 1) {
                return resolvedPeriod
                    .flatten()
                    .map {
                        PeriodAndLocationDto(
                            resolvedLocation[0],
                            it.first,
                            it.second
                        )
                    }
            }
            throw IllegalStateException(
                "size of resolvedLocation(${resolvedLocation.size}) != size of resolvedPeriod" +
                        "(:${resolvedPeriod.size})"
            )
        }

        return resolvedLocation.indices
            .flatMap { idx ->
                resolvedPeriod[idx].map {
                    PeriodAndLocationDto(
                        resolvedLocation[idx],
                        it.first,
                        it.second
                    )
                }
            }
    }

    fun resolvePeriod(raw: String): List<List<Pair<Day, Set<Int>>>> {

        val regex: Regex = Regex(
            """(?<day>[월화수목금토일])-?(?<periodList>([0-9]+,?)+)"""
        )
        val periodChunkList: List<String> = splitPeriod(raw)

        val resultList: MutableList<List<Pair<Day, Set<Int>>>> = mutableListOf()

        for (periodChunk in periodChunkList) {
            val chunk: List<Pair<Day, Set<Int>>> = regex.findAll(periodChunk).map { matchResult ->
                Pair(
                    Day.of(matchResult.groups["day"]!!.value),
                    matchResult.groups["periodList"]!!.value
                        .let { if (it.endsWith(",")) it.substring(0, it.length - 1) else it }
                        .split(",")
                        .map { it.toInt() }
                        .toSet()
                )
            }.toList()
            resultList.add(chunk)
        }

        return resultList
    }

    fun resolveLocation(raw: String): List<LectureLocationDto> {
        val ifOnlyKorean = Regex("""^[가-힣]+$""")
        val commonAddress = Regex(
            """(?<buildingName>[가-힣]+)(?<buildingNameOrB>[A-Z]*)(?<address>[0-9]{2,3})"""
        )

        val locationChunkList: List<String> = splitLocation(raw)

        val locationResultList: MutableList<LectureLocationDto> = mutableListOf()

        for (chunk in locationChunkList) {
            // when clause doesn't work like if-elseif
            // it works like if-if
            when {
                OnlineLectureType.allKoreanCodes()
                    .any { chunk.contains(it) } -> {
                    locationResultList.add(
                        OnlineLectureLocationDto(
                            OnlineLectureType.of(
                                OnlineLectureType.allKoreanCodes()
                                    .find{ chunk.contains(it)}!!
                            ),
                            !chunk.contains(Regex("""중복수강불가"""))
                        )
                    )
                }


                commonAddress.matches(chunk) -> {
                    val buildingName: String
                    val address: String
                    val matchGroup: MatchGroupCollection = (commonAddress
                        .find(chunk)
                        ?.groups
                        ?: throw IllegalStateException("unexpected form(input:$chunk)"))
                    if (
                        (matchGroup["buildingName"]!!.value == "공" &&
                                matchGroup["buildingNameOrB"]!!.value == "B") ||
                        (matchGroup["buildingName"]!!.value == "백" &&
                                matchGroup["buildingNameOrB"]!!.value == "S")
                    ) {
                        buildingName =
                            matchGroup["buildingName"]!!.value + matchGroup["buildingNameOrB"]!!.value
                        address = matchGroup["address"]!!.value
                    } else {
                        buildingName = matchGroup["buildingName"]!!.value
                        address =
                            matchGroup["buildingNameOrB"]!!.value + matchGroup["address"]!!.value
                    }

                    locationResultList.add(
                        OfflineLectureLocationDto(
                            YonseiBuilding.of(buildingName),
                            address
                        )
                    )
                }

                ifOnlyKorean.matches(chunk) -> {
                    locationResultList.add(
                        OfflineLectureLocationDto(
                            YonseiBuilding.of(chunk),
                            "000"
                        )
                    )
                }

                else -> throw IllegalStateException("(input:$chunk) doesn't matched cases")
            } // when scope end
        } // for scope end
        return locationResultList
    }
}
