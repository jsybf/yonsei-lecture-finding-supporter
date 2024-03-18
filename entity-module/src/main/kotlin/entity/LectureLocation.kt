package gitp.entity

import gitp.type.YonseiBuilding
import jakarta.persistence.*

@Entity
class LectureLocation(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long?,

    @Enumerated(value = EnumType.STRING)
    val building: YonseiBuilding,


    val address: String
) {
}