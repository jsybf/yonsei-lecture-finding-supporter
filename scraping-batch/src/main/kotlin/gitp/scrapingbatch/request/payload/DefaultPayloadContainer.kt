package gitp.yonseiprotohttp.payload

object DefaultPayloadContainer {
    val dtpPayload: Map<String, String> = mapOf(
        "%40d1%23dsNm" to "dsFaclyCd",
        "%40d1%23level" to "B",
        "%40d1%23lv1" to "s1",
        "%40d1%23lv3" to "%25",
        "%40d1%23univGbn" to "A",
        "%40d1%23findAuthGbn" to "8",
        "%40d1%23sysinstDivCd" to "%25",
    )

    val dptGroupPayload: Map<String, String> = mapOf(
        "%40d1%23dsNm" to "dsUnivCd",
        "%40d1%23level" to "B",
        "%40d1%23lv1" to "s1",
        "%40d1%23lv2" to "%25",
        "%40d1%23lv3" to "%25",
        "%40d1%23univGbn" to "A",
        "%40d1%23findAuthGbn" to "8",
        "%40d1%23sysinstDivCd" to "%25"
    )

    val lecturePayload: Map<String, String> = mapOf(
        "%40d1%23campsBusnsCd" to "s1",
        "%40d1%23hy" to "",
        "%40d1%23cdt" to "%25",
        "%40d1%23kwdDivCd" to "1",
        "%40d1%23searchGbn" to "1",
        "%40d1%23kwd" to "",
        "%40d1%23allKwd" to "",
        "%40d1%23engChg" to "",
        "%40d1%23prnGbn" to "false",
        "%40d1%23lang" to "",
        "%40d1%23campsDivCd" to "S",
        "%40d1%23stuno" to ""
    )

    val mileagePayload: Map<String, String> = mapOf(
        "%40d1%23stuno" to "",
        "%40d1%23sysinstDivCd" to "H1",
        "%40d1%23appcsSchdlCd" to ""
    )

    val mileageSummaryPayload: Map<String, String> = mapOf(
        "%40d1%23sysinstDivCd" to "H1",
    )

    val syllabusPayload: Map<String, String> = mapOf(
        "%40d1%23sysinstDivCd" to "H1",
    )

    val commonPayload: Map<String, String> = mapOf(
        "_menuId" to "MTA5MzM2MTI3MjkzMTI2NzYwMDA%3D",
        "_menuNm" to "",
        "_pgmId" to "NDE0MDA4NTU1NjY%3D",
        "%40d%23" to "%40d1%23",
        "%40d1%23" to "dmCond",
        "%40d1%23tp" to "dm"
    )

}