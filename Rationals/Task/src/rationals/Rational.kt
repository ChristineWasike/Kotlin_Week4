package rationals

import java.math.BigInteger


// Creating the Rational Class with guided values for the numerator and denominator as BigIntegers
@Suppress("DataClassPrivateConstructor")
data class Rational
private constructor(val numerator: BigInteger, val denominator: BigInteger) {
    companion object {
        fun initialize(numerator: BigInteger, denominator: BigInteger): Rational {
            // Throwing an IllegalArgumentException when the denominator is 0
            require(denominator != BigInteger.ZERO) { "Denominator must be non-zero" }
            return normalization(numerator, denominator)
        }

        // Initialization of the normalization function
        private fun normalization(numerator: BigInteger, denominator: BigInteger): Rational {

            // Establishing the greatest divisor to simplify the rational number
            val greatestCommonDivisor = numerator.gcd(denominator)

            val newNumerator = numerator / greatestCommonDivisor
            val newDenominator = denominator / greatestCommonDivisor

            // Accounting for the denominator always being positive
            val sign = denominator.signum().toBigInteger()

            return initialize(newNumerator * sign, newDenominator * sign)
        }

    }
}

// Defining the extension functions on DIVISION for the different digit representations
// Integer

// BigInteger
infix fun BigInteger.divBy(denominator: BigInteger): Rational {
    return Rational.initialize(this, denominator)
}

//



fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println("912016490186296920119201192141970416029".toBigInteger() divBy
            "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2)
}