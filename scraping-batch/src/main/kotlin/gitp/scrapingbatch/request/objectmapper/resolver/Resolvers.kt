package gitp.scrapingbatch.request.objectmapper.resolver

import gitp.scrapingbatch.dto.response.location.LectureLocationDto
import gitp.scrapingbatch.dto.response.location.OfflineLectureLocationDto
import gitp.scrapingbatch.dto.response.location.OnlineLectureLocationDto
import gitp.scrapingbatch.dto.response.location.PeriodAndLocationDto
import gitp.scrapingbatch.exception.ResolutionException
import gitp.type.Day
import gitp.type.MajorType
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
            throw ResolutionException(
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
                if (matchResult.groups["day"]!!.value == "") {
                    throw ResolutionException("can't extract day input:($raw)")
                }
                if (matchResult.groups["periodList"]!!.value == "") {
                    throw ResolutionException("can't extract day input:($raw)")
                }
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
            """(?<buildingName>[가-힣]+)(?<buildingNameOrB>[A-Z]?)(?<address>[0-9]{2,3})"""
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
                                    .find { chunk.contains(it) }!!
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
                        ?: throw ResolutionException("unexpected form(input:$chunk)"))

                    if (
                        matchGroup["buildingName"]!!.value != "공" &&
                        matchGroup["buildingNameOrB"]!!.value == "B"
                    ) {
                        buildingName = matchGroup["buildingName"]!!.value
                        address =
                            matchGroup["buildingNameOrB"]!!.value + matchGroup["address"]!!.value
                    } else {
                        buildingName =
                            matchGroup["buildingName"]!!.value + matchGroup["buildingNameOrB"]!!.value
                        address =
                            matchGroup["address"]!!.value
                    }

                    locationResultList.add(
                        OfflineLectureLocationDto(
                            getYonseiBuildingAdaptor(buildingName),
                            address
                        )
                    )
                }

                ifOnlyKorean.matches(chunk) -> {
                    locationResultList.add(
                        OfflineLectureLocationDto(
                            getYonseiBuildingAdaptor(chunk),
                            "000"
                        )
                    )
                }

                else -> throw ResolutionException("(input:$chunk) doesn't matched cases")
            } // when scope end
        } // for scope end
        return locationResultList
    }

    private fun getYonseiBuildingAdaptor(koreanCode: String): YonseiBuilding {
        try {
            return YonseiBuilding.of(koreanCode)
        } catch (e: Exception) {
            throw ResolutionException("no matching koreanCode for $koreanCode")
        }
    }

    fun resolveTotalCreditRatio(raw: String): Pair<Int, Int> {
        val expectedForm: Regex = Regex(
            """(?<currentCredit>[0-9]+)/(?<graduateCredit>[0-9]+)"""
        )

        val matchGroup: MatchGroupCollection = (expectedForm
            .find(raw)
            ?.groups
            ?: throw ResolutionException("unexpected form:($raw)"))

        return Pair(
            matchGroup["currentCredit"]!!.value.toInt(),
            matchGroup["graduateCredit"]!!.value.toInt()
        )
    }

    fun resolveMajor(raw: String): MajorType {
        val expectedForm: Regex = Regex(
            """(?<ifMajored>[YN])\((?<ifProtected>[YN])\)"""
        )
        val matchGroup: MatchGroupCollection = (expectedForm
            .find(raw)
            ?.groups
            ?: throw ResolutionException("unexpected form:($raw)"))

        return if (matchGroup["ifMajored"]!!.value == "N") {
            MajorType.NOT_MAJOR
        } else if (matchGroup["ifMajored"]!!.value == "Y"
            && matchGroup["ifProtected"]!!.value == "Y"
        ) {
            MajorType.MAJOR
        } else if (matchGroup["ifMajored"]!!.value == "Y"
            && matchGroup["ifProtected"]!!.value == "N"
        ) {
            MajorType.NOT_PROTECTED
        } else {
            throw ResolutionException("unexpected value:($raw)")
        }
    }
}
