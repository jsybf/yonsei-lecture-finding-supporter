package gitp.scrapingbatch.request.objectmapper.resolver

import gitp.scrapingbatch.dto.response.location.LectureLocationDto
import gitp.scrapingbatch.dto.response.location.OfflineLectureLocationDto
import gitp.scrapingbatch.dto.response.location.OnlineLectureLocationDto
import gitp.scrapingbatch.dto.response.location.PeriodAndLocationDto
import gitp.type.Day
import gitp.type.OnlineLectureType
import gitp.type.YonseiBuilding

object Resolvers {

    fun resolvePeriodAndLocation(
        rawPeriod: String,
        rawLocation: String
    ): List<PeriodAndLocationDto> {
        val resolvedLocation: List<LectureLocationDto> = resolveLocation(rawLocation)
        val resolvedPeriod: List<Pair<Day, Set<Int>>> = resolvePeriod(rawPeriod)

        if (resolvedLocation.size != resolvedPeriod.size) {
            if (resolvedLocation.size == 1) {
                return resolvedPeriod.indices
                    .map {
                        PeriodAndLocationDto(
                            resolvedLocation[0],
                            resolvedPeriod[it].first,
                            resolvedPeriod[it].second
                        )
                    }
                    .toList()
            }
            throw IllegalStateException(
                "size of resolvedLocation(${resolvedLocation.size}) != size of resolvedPeriod" +
                        "(:${resolvedPeriod.size})"
            )
        }

        return resolvedLocation.indices
            .map {
                PeriodAndLocationDto(
                    resolvedLocation[it],
                    resolvedPeriod[it].first,
                    resolvedPeriod[it].second
                )
            }.toList()
    }

    fun resolveLocation(raw: String): List<LectureLocationDto> {
        val locationGroupExtractor: Regex = Regex("""[가-힣a-zA-Z0-9]+""")
        val ifOnlyKorean: Regex = Regex("""^[가-힣]+$""")
        val commonAddress: Regex = Regex(
            """(?<buildingName>[가-힣]+)(?<buildingNameOrB>[A-Z]*)(?<address>[0-9]{2,3})"""
        )

        val locationChunkList: List<String> = locationGroupExtractor
            .findAll(raw)
            .map { it.value }
            .toList()

        val locationResultList: MutableList<LectureLocationDto> = mutableListOf()

        for (chunk in locationChunkList) {
            // when clause doesn't work like if-elseif
            // it works like if-if
            when {
                chunk == "동영상콘텐츠" || chunk == "실시간콘텐츠" || chunk == "동영상" -> {
                    locationResultList.add(
                        OnlineLectureLocationDto(
                            OnlineLectureType.of(chunk)
                        )
                    )
                }

                chunk == "중복수강불가" -> {
                    locationResultList.add(
                        OnlineLectureLocationDto(
                            (locationResultList.removeLast() as OnlineLectureLocationDto).type,
                            overlapAllowed = false
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
                            null
                        )
                    )
                }

                else -> throw IllegalStateException("(input:$chunk) doesn't matched cases")
            } // when scope end
        } // for scope end
        return locationResultList.distinct()
    }

    /**
     * form of period in json response has some forms
     * form1: 월1,2(수3,4)
     * form2: 월1,2/목2
     * form3: 월1,2/(목2)/금3
     */

    fun resolvePeriod(raw: String): List<Pair<Day, Set<Int>>> {
        val periodGroupExtractor: Regex = Regex("""[가-힣]([0-9]+,?)+""")
        val dayExtractor: Regex = Regex("""[월화수목금토일]""")
        val periodExtractor: Regex = Regex("""[0-9]+""")

        val periodGroupList = periodGroupExtractor.findAll(raw).map { it.value }.toList()

        val periodMap: MutableMap<Day, MutableSet<Int>> = mutableMapOf()
        val periodList: MutableList<Pair<Day, Set<Int>>> = mutableListOf()

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

            periodList.add(Pair(day, periodSet))
        }

        return periodList
    }

}