@file:Suppress("StringLiteralDuplication")

package io.mehow.ruler

import io.mehow.ruler.ImperialLengthUnit.Foot
import io.mehow.ruler.ImperialLengthUnit.Inch
import io.mehow.ruler.ImperialLengthUnit.Mile
import io.mehow.ruler.ImperialLengthUnit.Yard
import io.mehow.ruler.SiLengthUnit.Gigameter
import io.mehow.ruler.SiLengthUnit.Kilometer
import io.mehow.ruler.SiLengthUnit.Megameter
import io.mehow.ruler.SiLengthUnit.Meter
import io.mehow.ruler.SiLengthUnit.Micrometer
import io.mehow.ruler.SiLengthUnit.Millimeter
import io.mehow.ruler.SiLengthUnit.Nanometer
import java.math.BigDecimal
import java.math.RoundingMode.DOWN
import kotlin.Long.Companion.MAX_VALUE
import kotlin.Long.Companion.MIN_VALUE

class Distance private constructor(
  val metersPart: Long = 0L,
  val nanosPart: Long = 0L,
) : Comparable<Distance> {
  val exactTotalMeters = metersPart.toBigDecimal() +
      (nanosPart.toDouble() / nanosInMeter).toBigDecimal()

  fun <T> toLength(unit: T): Length<T> where T : Enum<T>, T : LengthUnit<T> {
    return Length(this, unit)
  }

  operator fun plus(other: Distance): Distance {
    return create(
        metersPart.safeAdd(other.metersPart),
        nanosPart.safeAdd(other.nanosPart)
    )
  }

  operator fun plus(length: Length<*>): Distance {
    return this + length.distance
  }

  operator fun minus(other: Distance): Distance {
    return create(
        metersPart.safeSubtract(other.metersPart),
        nanosPart.safeSubtract(other.nanosPart)
    )
  }

  operator fun minus(length: Length<*>): Distance {
    return this - length.distance
  }

  operator fun times(multiplicand: Int): Distance {
    return this * multiplicand.toLong()
  }

  operator fun times(multiplicand: Long): Distance {
    return when (multiplicand) {
      0L -> zero
      1L -> this
      else -> create(exactTotalMeters * multiplicand.toBigDecimal())
    }
  }

  operator fun times(multiplicand: Float): Distance {
    return this * multiplicand.toDouble()
  }

  operator fun times(multiplicand: Double): Distance {
    return when (multiplicand) {
      0.0 -> zero
      1.0 -> this
      else -> create(exactTotalMeters * multiplicand.toBigDecimal())
    }
  }

  operator fun div(divisor: Int): Distance {
    return this / divisor.toLong()
  }

  operator fun div(divisor: Long): Distance {
    require(divisor != 0L) { "Cannot divide by 0." }

    return if (divisor == 1L) this
    else create(exactTotalMeters.divide(divisor.toBigDecimal(), DOWN))
  }

  operator fun div(divisor: Float): Distance {
    return this / divisor.toDouble()
  }

  operator fun div(divisor: Double): Distance {
    require(divisor != 0.0) { "Cannot divide by 0" }

    return if (divisor == 1.0) this
    else create(exactTotalMeters.divide(divisor.toBigDecimal(), DOWN))
  }

  operator fun unaryMinus(): Distance {
    return this * -1
  }

  override fun compareTo(other: Distance): Int {
    val cmp = metersPart.compareTo(other.metersPart)
    return if (cmp != 0) cmp else nanosPart.compareTo(other.nanosPart)
  }

  override fun equals(other: Any?): Boolean {
    if (other !is Distance) return false
    return metersPart == other.metersPart && nanosPart == other.nanosPart
  }

  override fun hashCode(): Int {
    return 31 * metersPart.hashCode() + nanosPart.hashCode()
  }

  override fun toString(): String {
    return "Distance(meters=$metersPart, nanometers=$nanosPart)"
  }

  companion object {
    private const val nanosInMeter = 1_000_000_000L
    private val bigNanosInMeter = nanosInMeter.toBigInteger()

    @JvmStatic val min = Distance(MIN_VALUE, 0L)

    @JvmStatic val zero = Distance()

    @JvmStatic val max = Distance(MAX_VALUE, nanosInMeter - 1)

    internal fun create(meters: BigDecimal): Distance {
      val nanos = meters.movePointRight(9).toBigInteger()
      val divRem = nanos.divideAndRemainder(bigNanosInMeter)
      check(divRem[0].bitLength() <= 63) { "Exceeded distance capacity: $nanos" }
      val storedMeters = divRem[0].toLong()
      val storedNanometers = divRem[1].toLong()
      return create(storedMeters, storedNanometers)
    }

    @JvmStatic fun create(
      meters: Long = 0,
      nanometers: Long = 0,
    ): Distance {
      var meterPart = nanometers / nanosInMeter
      var nanoPart = nanometers % nanosInMeter
      if (nanoPart < 0) {
        nanoPart += nanosInMeter
        meterPart--
      }

      val totalMeters = meters.safeAdd(meterPart)
      val totalNanometers = nanoPart

      require(totalNanometers in 0..999_999_999) { "Nanometers must be between 0 and 1m" }

      return Distance(totalMeters, totalNanometers)
    }

    @JvmStatic fun of(value: Long, unit: LengthUnit<*>) = unit.toDistance(value)

    @JvmStatic fun ofGigameters(value: Long) = of(value, Gigameter)

    @JvmStatic fun ofMegameters(value: Long) = of(value, Megameter)

    @JvmStatic fun ofKilometers(value: Long) = of(value, Kilometer)

    @JvmStatic fun ofMeters(value: Long) = of(value, Meter)

    @JvmStatic fun ofMillimeters(value: Long) = of(value, Millimeter)

    @JvmStatic fun ofMicrometers(value: Long) = of(value, Micrometer)

    @JvmStatic fun ofNanometers(value: Long) = of(value, Nanometer)

    @JvmStatic fun ofMiles(value: Long) = of(value, Mile)

    @JvmStatic fun ofYards(value: Long) = of(value, Yard)

    @JvmStatic fun ofFeet(value: Long) = of(value, Foot)

    @JvmStatic fun ofInches(value: Long) = of(value, Inch)

    @JvmStatic fun of(value: Double, unit: LengthUnit<*>) = unit.toDistance(value)

    @JvmStatic fun ofGigameters(value: Double) = of(value, Gigameter)

    @JvmStatic fun ofMegameters(value: Double) = of(value, Megameter)

    @JvmStatic fun ofKilometers(value: Double) = of(value, Kilometer)

    @JvmStatic fun ofMeters(value: Double) = of(value, Meter)

    @JvmStatic fun ofMillimeters(value: Double) = of(value, Millimeter)

    @JvmStatic fun ofMicrometers(value: Double) = of(value, Micrometer)

    @JvmStatic fun ofNanometers(value: Double) = of(value, Nanometer)

    @JvmStatic fun ofMiles(value: Double) = of(value, Mile)

    @JvmStatic fun ofYards(value: Double) = of(value, Yard)

    @JvmStatic fun ofFeet(value: Double) = of(value, Foot)

    @JvmStatic fun ofInches(value: Double) = of(value, Inch)
  }
}
