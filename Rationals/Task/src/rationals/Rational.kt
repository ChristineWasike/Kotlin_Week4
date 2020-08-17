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
        fun normalization(numerator: BigInteger, denominator: BigInteger): Rational {

            // Establishing the greatest divisor to simplify the rational number
            val greatestCommonDivisor = numerator.gcd(denominator)

            val newNumerator = numerator / greatestCommonDivisor
            val newDenominator = denominator / greatestCommonDivisor

            // Accounting for the denominator always being positive
            val sign = denominator.signum().toBigInteger()

            val pair = Pair(newNumerator * sign, newDenominator * sign)
            return Rational(pair.first, pair.second)
        }
    }

    private fun rationalToStringConverter(numeratorOne: BigInteger, denominatorOne: BigInteger): String {

        if (denominatorOne == BigInteger.ONE) return "$numeratorOne"

        var numeratorInt = numeratorOne.toInt()
        var denominatorInt = denominatorOne.toInt()

        if (numeratorInt > denominatorInt) {
            for (i in 2..denominatorInt) {
                if (numeratorInt % i == 0 && i == 0) {
                    numeratorInt /= i
                    denominatorInt /= i

                    return rationalToStringConverter(numeratorInt.toBigInteger(), denominatorInt.toBigInteger())
                }
                if (numeratorInt < 0) {
                    return "$numeratorInt/$denominatorInt"
                }
                return "$numeratorInt/$denominatorInt"
            }
        } else if (denominatorInt > numeratorInt) {
            for (i in 2..numeratorInt) {
                if (numeratorInt % i == 0 && i == 0) {
                    numeratorInt /= i
                    denominatorInt /= i

                    return rationalToStringConverter(numeratorInt.toBigInteger(), denominatorInt.toBigInteger())
                }
            }
            if (numeratorInt < 0) {
                return "$numeratorInt/$denominatorInt"
            }
            return "$numeratorInt/$denominatorInt"
        }
        return "1"
    }

    // Overriding the String member function
    override fun toString(): String = rationalToStringConverter(numerator, denominator)

    // The Mathematical Operators as member functions

    // Unary Minus
    operator fun unaryMinus(): Rational {
        return initialize(-numerator, denominator)
    }

    // Addition
    operator fun plus(second: Rational): Rational {
        val newNumerator = numerator * second.denominator + denominator * second.numerator
        val newDenominator = denominator * second.denominator
        return initialize(newNumerator, newDenominator)
    }

    // Subtraction
    operator fun minus(second: Rational): Rational {
        val newNumerator = numerator * second.denominator - denominator * second.numerator
        val newDenominator = denominator * second.denominator
        return initialize(newNumerator, newDenominator)
    }

    // Division
    operator fun div(second: Rational): Rational {
        // Reciprocation action here
        val newNumerator = numerator * second.denominator
        val newDenominator = denominator * second.numerator
        return initialize(newNumerator, newDenominator)
    }

    // Multiplication
    operator fun times(second: Rational): Rational {
        //
        val newNumerator = numerator * second.numerator
        val newDenominator = denominator * second.denominator

        return initialize(newNumerator, newDenominator)
    }

    operator fun compareTo(second: Rational): Int {
        return (numerator * second.denominator - second.numerator * denominator).signum()
    }

    operator fun rangeTo(second: Rational): Pair<Rational, Rational> {
        return Pair(this, second)
    }


}

// Comparing the range
operator fun Pair<Rational, Rational>.contains(rational: Rational): Boolean {
    if (rational > this.first && rational < this.second) {
        return true
    }
    return false
}

// Defining the extension functions on DIVISION for the different digit representations
// Integer
infix fun Int.divBy(denominator: Int): Rational {
    return Rational.initialize(this.toBigInteger(), denominator.toBigInteger())
}

// BigInteger
infix fun BigInteger.divBy(denominator: BigInteger): Rational {
    return Rational.initialize(this, denominator)
}

// Long
infix fun Long.divBy(denominator: Long): Rational {
    return Rational.initialize(this.toBigInteger(), denominator.toBigInteger())
}


fun String.toRational(): Rational {
    if (!contains("/")) {
        return Rational.initialize(toBigInteger(), BigInteger.ONE)
    }
    val r = this.split("/")
    return Rational.initialize(r[0].toBigInteger(), r[1].toBigInteger())
}

fun main() {
    val numerator = 0
    val denominator = 68
    val rationalNumber = Rational.initialize(numerator.toBigInteger(), denominator.toBigInteger())
//    println(rationalNumber.toString())
    println(rationalNumber in numerator..)
}