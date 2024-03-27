package gitp.entity

import jakarta.persistence.*

@Entity
class ObjectMappingError(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long?,
    val exceptionMessage: String,
    @Column(columnDefinition = "TEXT")
    val responseJson: String,
    val jobExecutionId: Long
) {

}