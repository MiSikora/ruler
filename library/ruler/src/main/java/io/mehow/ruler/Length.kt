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

class Length<T> internal constructor(
  val distance: Distance,
  val unit: T
) : Comparable<Length<*>> where T : Enum<T>, T : LengthUnit<T> {
  val measuredLength = unit.toMeasuredLength(distance.exactTotalMeters)

  fun <R> withUnit(unit: R): Length<R> where R : Enum<R>, R : LengthUnit<R> {
    return Length(distance, unit)
  }

  fun withAutoUnit(): Length<T> {
    return withUnit(unit.single { it.appliesRangeTo(distance.exactTotalMeters.abs()) })
  }

  @JvmSynthetic fun coerceUnitIn(range: ClosedRange<T>): Length<T> {
    require(!range.isEmpty()) { "Range cannot be empty!" }
    return when {
      unit > range.endInclusive -> Length(distance, range.endInclusive)
      unit < range.start -> Length(distance, range.start)
      else -> this
    }
  }

  fun coerceUnitIn(min: T, max: T): Length<T> {
    return coerceUnitIn(min..max)
  }

  fun coerceUnitAtLeastTo(min: T): Length<T> {
    return if (unit < min) Length(distance, min) else this
  }

  fun coerceUnitAtMostTo(max: T): Length<T> {
    return if (unit > max) Length(distance, max) else this
  }

  operator fun plus(other: Length<*>): Length<T> {
    return (distance + other.distance).toLength(unit)
  }

  operator fun plus(distance: Distance): Length<T> {
    return (this.distance + distance).toLength(unit)
  }

  operator fun minus(other: Length<*>): Length<T> {
    return (distance - other.distance).toLength(unit)
  }

  operator fun minus(distance: Distance): Length<T> {
    return (this.distance - distance).toLength(unit)
  }

  operator fun times(multiplicand: Int): Length<T> {
    return (distance * multiplicand).toLength(unit)
  }

  operator fun times(multiplicand: Long): Length<T> {
    return (distance * multiplicand).toLength(unit)
  }

  operator fun times(multiplicand: Float): Length<T> {
    return (distance * multiplicand).toLength(unit)
  }

  operator fun times(multiplicand: Double): Length<T> {
    return (distance * multiplicand).toLength(unit)
  }

  operator fun div(multiplicand: Int): Length<T> {
    return (distance / multiplicand).toLength(unit)
  }

  operator fun div(multiplicand: Long): Length<T> {
    return (distance / multiplicand).toLength(unit)
  }

  operator fun div(multiplicand: Float): Length<T> {
    return (distance / multiplicand).toLength(unit)
  }

  operator fun div(multiplicand: Double): Length<T> {
    return (distance / multiplicand).toLength(unit)
  }

  operator fun unaryMinus(): Length<T> {
    return this * -1
  }

  override fun compareTo(other: Length<*>): Int {
    return distance.compareTo(other.distance)
  }

  override fun equals(other: Any?): Boolean {
    if (other !is Length<*>) return false
    return distance == other.distance && unit == other.unit
  }

  override fun hashCode(): Int {
    return 31 * distance.hashCode() + unit.hashCode()
  }

  override fun toString(): String {
    return "Length(measuredLength=$measuredLength, unit=$unit)"
  }

  companion object {
    @JvmStatic fun <T> of(value: Long, unit: T): Length<T> where T : Enum<T>, T : LengthUnit<T> {
      return Length(unit.toDistance(value), unit)
    }

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

    @JvmStatic fun <T> of(value: Double, unit: T): Length<T> where T : Enum<T>, T : LengthUnit<T> {
      return Length(unit.toDistance(value), unit)
    }

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
