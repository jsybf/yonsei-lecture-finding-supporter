package gitp.entity

import jakarta.persistence.*

@Entity
class Professor(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long?,

    @Column(length = 30)
    val name: String
) {
}