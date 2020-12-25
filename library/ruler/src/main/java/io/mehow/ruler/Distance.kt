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

public class Distance private constructor(
  public val metersPart: Long = 0L,
  public val nanosPart: Long = 0L,
) : Comparable<Distance> {
  public val exactTotalMeters: BigDecimal =
    metersPart.toBigDecimal() + (nanosPart.toDouble() / nanosInMeter).toBigDecimal()

  public fun <T> toLength(unit: T): Length<T> where T : Enum<T>, T : LengthUnit<T> {
    return Length(this, unit)
  }

  public operator fun plus(other: Distance): Distance {
    return create(
        metersPart.safeAdd(other.metersPart),
        nanosPart.safeAdd(other.nanosPart)
    )
  }

  public operator fun plus(length: Length<*>): Distance {
    return this + length.distance
  }

  public operator fun minus(other: Distance): Distance {
    return create(
        metersPart.safeSubtract(other.metersPart),
        nanosPart.safeSubtract(other.nanosPart)
    )
  }

  public operator fun minus(length: Length<*>): Distance {
    return this - length.distance
  }

  public operator fun times(multiplicand: Int): Distance {
    return this * multiplicand.toLong()
  }

  public operator fun times(multiplicand: Long): Distance {
    return when (multiplicand) {
      0L -> Zero
      1L -> this
      else -> create(exactTotalMeters * multiplicand.toBigDecimal())
    }
  }

  public operator fun times(multiplicand: Float): Distance {
    return this * multiplicand.toDouble()
  }

  public operator fun times(multiplicand: Double): Distance {
    return when (multiplicand) {
      0.0 -> Zero
      1.0 -> this
      else -> create(exactTotalMeters * multiplicand.toBigDecimal())
    }
  }

  public operator fun div(divisor: Int): Distance {
    return this / divisor.toLong()
  }

  public operator fun div(divisor: Long): Distance {
    require(divisor != 0L) { "Cannot divide by 0." }

    return if (divisor == 1L) this
    else create(exactTotalMeters.divide(divisor.toBigDecimal(), DOWN))
  }

  public operator fun div(divisor: Float): Distance {
    return this / divisor.toDouble()
  }

  public operator fun div(divisor: Double): Distance {
    require(divisor != 0.0) { "Cannot divide by 0" }

    return if (divisor == 1.0) this
    else create(exactTotalMeters.divide(divisor.toBigDecimal(), DOWN))
  }

  public operator fun unaryMinus(): Distance {
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

  public companion object {
    private const val nanosInMeter = 1_000_000_000L

    @JvmField public val Min: Distance = Distance(MIN_VALUE, 0L)

    @JvmField public val Zero: Distance = Distance()

    @JvmField public val Max: Distance = Distance(MAX_VALUE, nanosInMeter - 1)

    internal fun create(meters: BigDecimal): Distance {
      val nanos = meters.movePointRight(9).toBigInteger()
      val divRem = nanos.divideAndRemainder(nanosInMeter.toBigInteger())
      check(divRem[0].bitLength() <= 63) { "Exceeded distance capacity: $nanos" }
      val storedMeters = divRem[0].toLong()
      val storedNanometers = divRem[1].toLong()
      return create(storedMeters, storedNanometers)
    }

    @JvmStatic public fun create(
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

    @JvmStatic public fun of(value: Long, unit: LengthUnit<*>): Distance = create(
        meters = value.toBigDecimal() * unit.meterRatio,
    )

    @JvmStatic public fun ofGigameters(value: Long): Distance = of(value, Gigameter)

    @JvmStatic public fun ofMegameters(value: Long): Distance = of(value, Megameter)

    @JvmStatic public fun ofKilometers(value: Long): Distance = of(value, Kilometer)

    @JvmStatic public fun ofMeters(value: Long): Distance = of(value, Meter)

    @JvmStatic public fun ofMillimeters(value: Long): Distance = of(value, Millimeter)

    @JvmStatic public fun ofMicrometers(value: Long): Distance = of(value, Micrometer)

    @JvmStatic public fun ofNanometers(value: Long): Distance = of(value, Nanometer)

    @JvmStatic public fun ofMiles(value: Long): Distance = of(value, Mile)

    @JvmStatic public fun ofYards(value: Long): Distance = of(value, Yard)

    @JvmStatic public fun ofFeet(value: Long): Distance = of(value, Foot)

    @JvmStatic public fun ofInches(value: Long): Distance = of(value, Inch)

    @JvmStatic public fun of(value: Double, unit: LengthUnit<*>): Distance = create(
        meters = value.toBigDecimal() * unit.meterRatio,
    )

    @JvmStatic public fun ofGigameters(value: Double): Distance = of(value, Gigameter)

    @JvmStatic public fun ofMegameters(value: Double): Distance = of(value, Megameter)

    @JvmStatic public fun ofKilometers(value: Double): Distance = of(value, Kilometer)

    @JvmStatic public fun ofMeters(value: Double): Distance = of(value, Meter)

    @JvmStatic public fun ofMillimeters(value: Double): Distance = of(value, Millimeter)

    @JvmStatic public fun ofMicrometers(value: Double): Distance = of(value, Micrometer)

    @JvmStatic public fun ofNanometers(value: Double): Distance = of(value, Nanometer)

    @JvmStatic public fun ofMiles(value: Double): Distance = of(value, Mile)

    @JvmStatic public fun ofYards(value: Double): Distance = of(value, Yard)

    @JvmStatic public fun ofFeet(value: Double): Distance = of(value, Foot)

    @JvmStatic public fun ofInches(value: Double): Distance = of(value, Inch)
  }
}
