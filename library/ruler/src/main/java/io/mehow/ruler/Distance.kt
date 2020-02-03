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
import kotlin.Long.Companion.MAX_VALUE

class Distance private constructor(
  val metersPart: Long = 0L,
  val nanosPart: Long = 0L
) : Comparable<Distance> {
  internal val exactTotalMeters =
    metersPart.toBigDecimal() + (nanosPart.toDouble() / 1_000_000_000).toBigDecimal()
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
    @JvmStatic val Zero = Distance()

    @JvmStatic val Max = Distance(MAX_VALUE, 999_999_999)

    @JvmStatic fun create(
      meters: Long = 0,
      nanometers: Long = 0
    ): Distance {
      var meterPart = nanometers / 1_000_000_000
      var nanoPart = nanometers % 1_000_000_000
      if (nanoPart < 0) {
        nanoPart += 1_000_000_000
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
