package gitp.type

import jakarta.persistence.Embeddable

@Embeddable
data class Fraction(
    val denominator: Int,
    val numerator: Int
)
