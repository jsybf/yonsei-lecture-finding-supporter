package gitp.type

enum class Semester(val code: Int) {
    FIRST(10),
    SECOND(20);

    companion object {
        fun codeOf(code: Int): Semester {
            for (entry in entries) {
                if (entry.code == code) {
                    return entry
                }
            }

            throw IllegalStateException("can't match input code(:$code)")
        }
    }
}