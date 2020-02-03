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
import kotlin.Long.Companion.MAX_VALUE

class Distance private constructor(
  val metersPart: Long = 0L,
  val nanosPart: Long = 0L
) : Comparable<Distance> {
  internal val exactTotalMeters =
    metersPart.toBigDecimal() + (nanosPart.toDouble() / NanosInMeter).toBigDecimal()
  val totalMeters = exactTotalMeters.toDouble()

  fun <T> toLength(
    unit: T
  ): Length<T> where T : LengthUnit, T : Comparable<T>, T : Iterable<T> {
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
    require(multiplicand >= 0) { "Distance cannot be negative." }

    return when (multiplicand) {
      0L -> Zero
      1L -> this
      else -> create(exactTotalMeters * multiplicand.toBigDecimal())
    }
  }

  operator fun times(multiplicand: Float): Distance {
    return this * multiplicand.toDouble()
  }

  operator fun times(multiplicand: Double): Distance {
    require(multiplicand >= 0) { "Distance cannot be negative." }

    return when (multiplicand) {
      0.0 -> Zero
      1.0 -> this
      else -> create(exactTotalMeters * multiplicand.toBigDecimal())
    }
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
    private const val NanosInMeter = 1_000_000_000L
    private val bigNanosInMeter = NanosInMeter.toBigDecimal()

    @JvmStatic val Zero = Distance()

    @JvmStatic val Max = Distance(MAX_VALUE, NanosInMeter - 1)

    internal fun create(meters: BigDecimal): Distance {
      val storedMeters = meters.toBigInteger().longValueExact()
      val nanometers = (meters - storedMeters.toBigDecimal()) * bigNanosInMeter
      return create(storedMeters, nanometers.toLong())
    }

    @JvmStatic fun create(
      meters: Long = 0,
      nanometers: Long = 0
    ): Distance {
      var meterPart = nanometers / NanosInMeter
      var nanoPart = nanometers % NanosInMeter
      if (nanoPart < 0) {
        nanoPart += NanosInMeter
        meterPart--
      }

      val totalMeters = meters.safeAdd(meterPart)
      val totalNanometers = nanoPart

      require(totalMeters >= 0) { "Distance cannot be negative." }
      require(totalNanometers >= 0) { "Distance cannot be negative." }

      return Distance(totalMeters, totalNanometers)
    }

    @JvmStatic fun of(value: Long, unit: LengthUnit) = unit.toDistance(value)

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
  }
}
