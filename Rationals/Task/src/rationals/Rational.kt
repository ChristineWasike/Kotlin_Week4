package rationals

import java.math.BigInteger
import java.util.*

class Rational(numerator: BigInteger, denominator: BigInteger) : Comparable<Rational> {
    companion object {
        val ZERO = Rational(BigInteger.ZERO, BigInteger.ONE)
    }

    private val numerator: BigInteger
    private val denominator: BigInteger

    constructor(number: Int) : this(number.toBigInteger(), BigInteger.ONE)
    constructor(number: Long) : this(number.toBigInteger(), BigInteger.ONE)
    constructor(number: BigInteger) : this(number, BigInteger.ONE)

    init {
        // Throwing an IllegalArgumentException when the denominator is 0
        require(denominator != BigInteger.ZERO) { "Denominator must be non-zero" }
        val (normalizedNumerator, normalizedDenominator) = normalize(numerator, denominator)
        this.numerator = normalizedNumerator
        this.denominator = normalizedDenominator
    }

    private fun normalize(numerator: BigInteger, denominator: BigInteger): Pair<BigInteger, BigInteger> {
        // Establishing the greatest divisor to simplify the rational number
        val greatestCommonDivisor = numerator.gcd(denominator)

        val newNumerator = numerator / greatestCommonDivisor
        val newDenominator = denominator / greatestCommonDivisor

        // Accounting for the denominator always being positive
        val sign = denominator.signum().toBigInteger()

        val pair = Pair(newNumerator * sign, newDenominator * sign)
        return Pair(pair.first, pair.second)
    }

    private fun greatestCommonDivisor(m: BigInteger, n: BigInteger): BigInteger {
        // Getting the absolute values of both entries
        val a = m.abs()
        val b = n.abs()
        return if (b == BigInteger.ZERO) a else greatestCommonDivisor(b, a % b)
    }

    private fun leastCommonMultiple(m: BigInteger, n: BigInteger): BigInteger {
        val a = m.abs()
        val b = n.abs()
        return a * (b / greatestCommonDivisor(a, b))
    }

    operator fun unaryMinus(): Rational {
        return Rational(-numerator, denominator)
    }

    operator fun plus(second: Rational): Rational {
        val first = this

        // Edge cases
        if (first == Rational(BigInteger.ZERO, BigInteger.ONE)) return second
        if (second == Rational(BigInteger.ZERO, BigInteger.ONE)) return first

        // Finding the GCD of the numerators and denominators
        val gcdNumerator = greatestCommonDivisor(first.numerator, second.numerator)
        val gcdDenominator = greatestCommonDivisor(first.denominator, second.denominator)

        // Adding cross-product terms for numerators
        val newNumerator = gcdNumerator * ((first.numerator / gcdNumerator) * (second.denominator / gcdDenominator) +
                (second.numerator / gcdNumerator) * (first.denominator / gcdDenominator))

        val newDenominator = leastCommonMultiple(first.denominator, second.denominator)

        //val newNumerator = numerator * second.denominator + denominator * second.numerator
        //val newDenominator = denominator * second.denominator
        return Rational(newNumerator, newDenominator)
    }

    private fun negate(): Rational = Rational(-numerator, denominator)

    operator fun minus(b: Rational): Rational {
        return this.plus(b.negate())
    }

    operator fun times(second: Rational): Rational {
        val first = this
        // Simplifying the two rationals before multiplication
        val x = Rational(first.numerator, second.denominator)
        val y = Rational(second.numerator, first.denominator)
        return Rational(x.numerator * y.numerator, x.denominator * y.denominator)
    }

    private fun reciprocal(): Rational = Rational(denominator, numerator)

    operator fun div(b: Rational): Rational {
        val a = this
        return a.times(b.reciprocal())
    }

    override fun toString(): String =
            if (denominator == BigInteger.ONE) "$numerator" else "$numerator/$denominator"

    override fun hashCode(): Int {
        return Objects.hash(numerator, denominator)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        return compareTo(other as Rational) == 0
    }

    override operator fun compareTo(other: Rational): Int {
        return (numerator * other.denominator - other.numerator * denominator).signum()
    }


}

fun String.toRational(): Rational {
    if (!contains("/")) {
        return Rational(toBigInteger(), BigInteger.ONE)
    }
    val r = this.split("/")
    return Rational(r[0].toBigInteger(), r[1].toBigInteger())
}

// Defining the extension functions on DIVISION for the different digit representations
// Integer
infix fun Int.divBy(denominator: Int): Rational {
    return Rational(this) / Rational(denominator)
}

// BigInteger
infix fun BigInteger.divBy(denominator: BigInteger): Rational {
    return Rational(this) / Rational(denominator)
}

// Long
infix fun Long.divBy(denominator: Long): Rational {
    return Rational(this) / Rational(denominator)
}

fun main() {
    val r1 = 1 divBy 2
    val r2 = 2000000000L divBy 4000000000L
    println(r1 == r2)

    println((2 divBy 1).toString() == "2")

    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    println("1/2".toRational() - "1/3".toRational() == "1/6".toRational())
    println("1/2".toRational() + "1/3".toRational() == "5/6".toRational())

    println(-(1 divBy 2) == (-1 divBy 2))

    println((1 divBy 2) * (1 divBy 3) == "1/6".toRational())
    println((1 divBy 2) / (1 divBy 4) == "2".toRational())

    println((1 divBy 2) < (2 divBy 3))
    println((1 divBy 2) in (1 divBy 3)..(2 divBy 3))

    println(
            "912016490186296920119201192141970416029".toBigInteger() divBy
                    "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2
    )
}