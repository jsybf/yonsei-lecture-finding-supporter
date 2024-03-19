package gitp.entity

import gitp.type.OnlineLectureType
import gitp.type.YonseiBuilding
import jakarta.persistence.*

@Entity
class OnlineLectureLocation (
    @Enumerated(value = EnumType.STRING)
    val type: OnlineLectureType,

    val overlapAllowed: Boolean
): LectureLocation(null)