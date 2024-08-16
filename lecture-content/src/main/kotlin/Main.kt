package org.example

import com.fasterxml.jackson.databind.JsonNode
import gitp.scrapingbatch.dto.payload.DptGroupPayloadDto
import gitp.scrapingbatch.dto.payload.DptPayloadDto
import gitp.scrapingbatch.dto.payload.LecturePayloadDto
import gitp.scrapingbatch.dto.payload.SyllabusPayloadDto
import gitp.scrapingbatch.dto.response.*
import gitp.scrapingbatch.request.YonseiHttpClient
import gitp.scrapingbatch.request.YonseiUrlContainer
import gitp.scrapingbatch.utils.MyUtils
import gitp.type.Semester
import gitp.yonseiprotohttp.payload.PayloadBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Year

val dptGroupClient = YonseiHttpClient.of<DptGroupResponseDto>(
    YonseiUrlContainer.dptGroupUrl,
    MyUtils.getCommonObjectMapper()
) { jsonNode: JsonNode ->
    jsonNode.path("dsUnivCd")
}
val dptClient = YonseiHttpClient.of<DptResponseDto>(
    YonseiUrlContainer.dptUrl,
    MyUtils.getCommonObjectMapper()
) { jsonNode: JsonNode ->
    jsonNode.path("dsFaclyCd")
}
val lectureClient2 = YonseiHttpClient.of<SimpleLectureResponseDto>(
    YonseiUrlContainer.lectureUrl,
    MyUtils.getCommonObjectMapper(),
    mapOf("rmvlcYnNm" to "폐강"),
) { jsonNode: JsonNode ->
    jsonNode.path("dsSles251")
}
val syllabusClient = YonseiHttpClient.of<SyllabusResponseDto>(
    "https://underwood1.yonsei.ac.kr/sch/sles/SlessyCtr/findSubjtDescList.do",
    MyUtils.getCommonObjectMapper(),
) { jsonNode: JsonNode ->
    jsonNode.path("_METADATA_")
}

fun requestSimpleLecture(dptGroupId: String, dptId: String, year: Year, semester: Semester): List<SimpleLectureResponseDto> {
    val payload: String = PayloadBuilder.toPayload(
        LecturePayloadDto(
            Year.of(2024),
            Semester.SECOND,
            dptGroupId,
            dptId
        )
    )
    return lectureClient2.retrieveAndMapToList(payload)
}

fun requestDpt(dptGroupId: String, year: Year, semester: Semester): List<DptResponseDto> {
    val payload: String = PayloadBuilder.toPayload(
        DptPayloadDto(
            dptGroupId,
            Year.of(2024),
            Semester.SECOND
        )
    )

    return dptClient.retrieveAndMapToList(payload)

}

fun requestSyllabus(lectureResponseDto: SimpleLectureResponseDto, year: Year, semester: Semester): String {
    val payload: String = PayloadBuilder.toPayload(
        SyllabusPayloadDto(Year.of(2024), Semester.SECOND, lectureResponseDto.lectureId, lectureResponseDto.name)
    )

    return syllabusClient.retrieveAndMap(payload)?.content ?: ""
}

fun main() {
    val logger: Logger = LoggerFactory.getLogger("main")

    // 큰 분류
    val payloads = PayloadBuilder.toPayload(
        DptGroupPayloadDto(
            Year.of(2024),
            Semester.SECOND
        )
    )
    val dptGroupDtoList: List<DptGroupResponseDto> = dptGroupClient.retrieveAndMapToList(payloads)
    val lectureIdSyllabusPairList: MutableList<Pair<String, String>> = mutableListOf()
    val year: Year = Year.of(2024)
    val semester: Semester = Semester.SECOND
    for (dptGroup in dptGroupDtoList.slice(0..4)) {
        val dptList: List<DptResponseDto> = requestDpt(dptGroup.dptGroupId, year, semester)
        logger.info("complete requesting dpt of dpt group:[${dptGroup.dptGroupName}]")

        val lectureList: List<SimpleLectureResponseDto> = dptList
            .map { requestSimpleLecture(dptGroup.dptGroupId, it.dptId, year, semester) }
            .flatten()
        logger.info("complete requesting lecture of dpt group:[${dptGroup.dptGroupName}]")

        // 강의개요
        val syllabusList: List<String> = lectureList
            .map { requestSyllabus(it, year, semester) }
        logger.info("complete requesting syllabus of dpt group:[${dptGroup.dptGroupName}]")


        if (syllabusList.size != lectureList.size)
            throw IllegalStateException("size of syllabusList should be same with size of lectureResponseDtoList")
        val map: List<Pair<String, String>> = lectureList
            .zip(syllabusList)
            .map { "${it.first.lectureId.mainId}-${it.first.lectureId.classDivisionId}-00" to (it.second) }
        lectureIdSyllabusPairList += map
    }
    // for (dptGroup in dptGroupDtoList.slice(1..1)) {
    //     val dptPayload: String = PayloadBuilder.toPayload(
    //         DptPayloadDto(
    //             dptGroup.dptGroupId,
    //             Year.of(2024),
    //             Semester.SECOND
    //         )
    //     )
    //
    //     val lectureResponseDtoList: List<SimpleLectureResponseDto> = dptClient.retrieveAndMapToList(dptPayload)
    //         .map {
    //             PayloadBuilder.toPayload(
    //                 LecturePayloadDto(
    //                     Year.of(2024),
    //                     Semester.SECOND,
    //                     dptGroup.dptGroupId,
    //                     it.dptId
    //                 )
    //             )
    //         }
    //         .map { lectureClient2.retrieveAndMapToList(it) }
    //         .flatten()
    //     println("requesting lecture complete")
    //
    //     val syllabusList: List<SyllabusResponseDto> = lectureResponseDtoList
    //         .map {
    //             PayloadBuilder.toPayload(
    //                 SyllabusPayloadDto(Year.of(2024), Semester.SECOND, it.lectureId, it.name)
    //             )
    //         }
    //         .map { syllabusClient.retrieveAndMap(it) }
    //     println("requesting syllabus complete")
    //
    //     if (syllabusList.size != lectureResponseDtoList.size)
    //         throw IllegalStateException("size of syllabusList should be same with size of lectureResponseDtoList")
    //     val map: List<Pair<String, String>> = lectureResponseDtoList
    //         .zip(syllabusList)
    //         .map { "${it.first.lectureId.mainId}-${it.first.lectureId.subId}-00" to (it?.second?.content ?: "") }
    //     lectureIdSyllabusPairList += map
    // }
    // 개별 강의
    lectureIdSyllabusPairList.forEach {
        println()
        println(it)
        println()
    }
    println("Hello World!")
}