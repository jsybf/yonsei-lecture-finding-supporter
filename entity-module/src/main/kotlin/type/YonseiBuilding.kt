package gitp.type

@Suppress("SpellCheckingInspection")
enum class YonseiBuilding(val koreanCode: String) {
    OESOL("외"),
    WIDANG("위"),
    ECO_MAIN("상본"),
    ECO_SUB("상별"),
    BUSINESS("경영"),
    SCIENCE("과"),
    SCIENCE_S("과S"),
    ENGINEERING_A("공A"),
    ENGINEERING_A_S("공AS"),
    ENGINEERING_B("공B"),
    ENGINEERING_C("공C"),
    ENGINEERING_D("공D"),
    GS("석"),
    WONDU("신"),
    YEONHUI("연"),
    GWANGBOK("광"),
    MUSIC("음"),
    SAMSUNG("삼"),
    EDU("교"),
    SPORT_EDU("체"),
    SPORT("스포츠"),
    BAEGYANG_S("백S"),
    BAEGYANG_N("백N"),
    BAEGYANG("백"),
    BAEGYANG_HALL("백양관강당"),
    APENJ("아"),
    BILLING("빌"),
    RUISE("루"),
    WONHUI("원"),
    SAECHEON("새천"),
    SEOKSANHALL("석산홀세미나실");

    companion object {
        fun of(koreanCode: String): YonseiBuilding {
            for (entry in entries) {
                if (entry.koreanCode == koreanCode) {
                    return entry
                }
            }

            throw NoSuchElementException("unexpected koreanCode(input:$koreanCode)")
        }
    }
}