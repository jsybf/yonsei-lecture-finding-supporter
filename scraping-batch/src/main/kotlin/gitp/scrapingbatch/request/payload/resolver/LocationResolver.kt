package gitp.scrapingbatch.request.payload.resolver

import gitp.scrapingbatch.request.payload.resolver.dto.LectureLocationDto
import gitp.scrapingbatch.request.payload.resolver.dto.OfflineLectureLocationDto
import gitp.scrapingbatch.request.payload.resolver.dto.OnlineLectureLocationDto
import gitp.scrapingbatch.request.payload.resolver.type.OnlineLectureType
import gitp.scrapingbatch.request.payload.resolver.type.YonseiBuilding

object LocationResolver {
    fun resolve(raw: String): List<LectureLocationDto> {
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
                    if (matchGroup["buildingName"]!!.value == "공"
                        && matchGroup["buildingNameOrB"]!!.value == "B"
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
        return locationResultList
    }
}