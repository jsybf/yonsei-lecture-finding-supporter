package gitp.entity

import jakarta.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
class LectureLocation(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long?,
) {
}