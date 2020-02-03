package io.mehow.ruler

import io.mehow.ruler.ImperialDistanceUnit.Foot
import io.mehow.ruler.ImperialDistanceUnit.Inch
import io.mehow.ruler.ImperialDistanceUnit.Mile
import io.mehow.ruler.ImperialDistanceUnit.Yard
import io.mehow.ruler.SiDistanceUnit.Gigameter
import io.mehow.ruler.SiDistanceUnit.Kilometer
import io.mehow.ruler.SiDistanceUnit.Megameter
import io.mehow.ruler.SiDistanceUnit.Meter
import io.mehow.ruler.SiDistanceUnit.Micrometer
import io.mehow.ruler.SiDistanceUnit.Millimeter
import io.mehow.ruler.SiDistanceUnit.Nanometer
import kotlin.Long.Companion.MAX_VALUE

class Length private constructor(
  val metersPart: Long = 0L,
  val nanosPart: Long = 0L
) : Comparable<Length> {
  internal val exactTotalMeters = metersPart.toBigDecimal() +
      (nanosPart.toDouble() / 1_000_000_000).toBigDecimal()
  val totalMeters = exactTotalMeters.toDouble()

  fun <T> toDistance(
    unit: T
  ): Distance<T> where T : DistanceUnit, T : Comparable<T>, T : Iterable<T> {
    return Distance(this, unit)
  }

  operator fun plus(other: Length): Length {
    return create(
        metersPart.safeAdd(other.metersPart),
        nanosPart.safeAdd(other.nanosPart)
    )
  }

  operator fun plus(distance: Distance<*>): Length {
    return this + distance.length
  }

  operator fun minus(other: Length): Length {
    return create(
        metersPart.safeSubtract(other.metersPart),
        nanosPart.safeSubtract(other.nanosPart)
    )
  }

  operator fun minus(distance: Distance<*>): Length {
    return this - distance.length
  }

  override fun compareTo(other: Length): Int {
    val cmp = metersPart.compareTo(other.metersPart)
    return if (cmp != 0) cmp else nanosPart.compareTo(other.nanosPart)
  }

  override fun equals(other: Any?): Boolean {
    if (other !is Length) return false
    return metersPart == other.metersPart && nanosPart == other.nanosPart
  }

  override fun hashCode(): Int {
    return 31 * metersPart.hashCode() + nanosPart.hashCode()
  }

  override fun toString(): String {
    return "Length(meters=$metersPart, nanometers=$nanosPart)"
  }

  companion object {
    @JvmStatic val Zero = Length()

    @JvmStatic val Max = Length(MAX_VALUE, 999_999_999)

    @JvmStatic fun create(
      meters: Long = 0,
      nanometers: Long = 0
    ): Length {
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

      return Length(totalMeters, totalNanometers)
    }

    @JvmStatic fun of(value: Long, unit: DistanceUnit) = unit.toLength(value)

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
