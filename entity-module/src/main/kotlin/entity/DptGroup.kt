package gitp.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class DptGroup(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long?,

    val dptGroupName: String,

    val dptGroupId: String
) {

}