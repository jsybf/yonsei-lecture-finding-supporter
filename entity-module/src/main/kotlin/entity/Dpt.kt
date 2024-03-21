package gitp.entity

import jakarta.persistence.*

@Entity
class Dpt(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long?,

    val dptName: String,

    val dptId: String,

    @ManyToOne
    val dptGroup: DptGroup

) {

}