package gitp.type

enum class Day(val koreanCode: String) {
    MON("월"),
    TUE("화"),
    WEN("수"),
    THU("목"),
    FRI("금"),
    SAT("토"),
    SUN("일");

    companion object {
        fun of(koreanCode: String): Day {
            for (entry in entries) {
                if (entry.koreanCode == koreanCode) {
                    return entry
                }
            }

            throw NoSuchElementException("argument $koreanCode didn't match")
        }
    }
}