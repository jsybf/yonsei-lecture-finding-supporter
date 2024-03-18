package gitp.entity

import jakarta.persistence.*

@Entity
class LectureId(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long?,

    @Column(length = 7)
    val mainId: String,

    @Column(length = 2)
    val classDivisionId: String,

    @Column(length = 2)
    val subId: String
) {
}