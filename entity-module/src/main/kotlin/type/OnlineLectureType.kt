package gitp.type

enum class OnlineLectureType(vararg koreanCodes: String) {
    VIDEO("동영상콘텐츠", "동영상"),
    LIVE_STREAMING("실시간온라인");

    val koreanCodeArr: Array<String> = arrayOf(*koreanCodes)

    companion object {
        fun of(koreanCode: String): OnlineLectureType {
            for (entry in entries) {
                if (entry.koreanCodeArr.contains(koreanCode)) {
                    return entry
                }
            }

            throw NoSuchElementException("unexpected korean code (input:$koreanCode)")
        }

        fun allKoreanCodes(): List<String> {
            return entries.flatMap { it.koreanCodeArr.toList() }
        }
    }
}
