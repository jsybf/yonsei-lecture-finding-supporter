package org.example

import com.fasterxml.jackson.databind.JsonNode
import com.mongodb.ConnectionString
import com.mongodb.kotlin.client.MongoClient
import com.mongodb.kotlin.client.MongoCollection
import com.mongodb.kotlin.client.MongoDatabase
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
import org.bson.BsonType
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonRepresentation
import org.bson.types.ObjectId
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
            year,
            semester,
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
            year,
            semester,
        )
    )

    return dptClient.retrieveAndMapToList(payload)

}

fun requestSyllabus(lectureResponseDto: SimpleLectureResponseDto, year: Year, semester: Semester): String {
    val payload: String = PayloadBuilder.toPayload(
        SyllabusPayloadDto(year, semester, lectureResponseDto.lectureId, lectureResponseDto.name)
    )

    return syllabusClient.retrieveAndMap(payload)?.content ?: ""
}

data class Syllabus(
    @BsonId
    @BsonRepresentation(BsonType.OBJECT_ID)
    val id: String?,
    val lectureCode: String,
    val syllabus: String
)

fun main() {
    val logger: Logger = LoggerFactory.getLogger("main")
    val year: Year = Year.of(2024)
    val semester: Semester = Semester.SECOND

    val hostUrl = "mongodb://43.201.1.128"
    val connectionString: ConnectionString = ConnectionString(hostUrl)
    val client = MongoClient.create(connectionString)
    val database: MongoDatabase = client.getDatabase("everytime")
    val collection: MongoCollection<Syllabus> = database.getCollection("syllabus")

    // 큰 분류
    val payloads = PayloadBuilder.toPayload(
        DptGroupPayloadDto(
            year,
            semester
        )
    )
    val dptGroupDtoList: List<DptGroupResponseDto> = dptGroupClient.retrieveAndMapToList(payloads)

    val lectureIdSyllabusPairList: MutableList<Pair<String, String>> = mutableListOf()


    for (dptGroup in dptGroupDtoList.slice(0..19)) {
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
        val map: List<Syllabus> = lectureList
            .zip(syllabusList)
            .map {
                Syllabus(
                    null,
                    "${it.first.lectureId.mainId}-${it.first.lectureId.classDivisionId}-00",
                    it.second
                )
            }
        if(map.isEmpty())
            continue
        collection.insertMany(map)
    }

    lectureIdSyllabusPairList.forEach {
        println()
        println(it)
        println()
    }
}