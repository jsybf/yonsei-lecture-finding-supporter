package gitp.entity

import gitp.type.YonseiBuilding
import jakarta.persistence.*

@Entity
class OfflineLectureLocation(
    @Enumerated(value = EnumType.STRING)
    val building: YonseiBuilding,


    val address: String
): LectureLocation(null)