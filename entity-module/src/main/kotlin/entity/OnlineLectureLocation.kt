package gitp.entity

import gitp.type.OnlineLectureType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Entity
class OnlineLectureLocation(
    @Enumerated(value = EnumType.STRING)
    val type: OnlineLectureType,

    val overlapAllowed: Boolean
) : LectureLocation(null)