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

public class Length<T> internal constructor(
  public val distance: Distance,
  public val unit: T,
) : Comparable<Length<*>> where T : Enum<T>, T : LengthUnit<T> {
  public val measure: BigDecimal = distance.meters.divide(unit.meterRatio, 9, DOWN)

  public fun <R> withUnit(unit: R): Length<R> where R : Enum<R>, R : LengthUnit<R> = Length(distance, unit)

  public fun withAutoUnit(): Length<T> = withUnit(unit.javaClass.enumConstants.single { distance in it })

  public fun coerceUnitIn(range: ClosedRange<T>): Length<T> = when {
    range.isEmpty() -> throw IllegalArgumentException("Range cannot be empty!")
    unit > range.endInclusive -> Length(distance, range.endInclusive)
    unit < range.start -> Length(distance, range.start)
    else -> this
  }

  public fun coerceUnitIn(min: T, max: T): Length<T> = coerceUnitIn(min..max)

  public fun coerceUnitAtLeastTo(min: T): Length<T> = when {
    unit < min -> Length(distance, min)
    else -> this
  }

  public fun coerceUnitAtMostTo(max: T): Length<T> = when {
    unit > max -> Length(distance, max)
    else -> this
  }

  public operator fun plus(other: Length<*>): Length<T> = (distance + other.distance).toLength(unit)

  public operator fun plus(distance: Distance): Length<T> = (this.distance + distance).toLength(unit)

  public operator fun minus(other: Length<*>): Length<T> = (distance - other.distance).toLength(unit)

  public operator fun minus(distance: Distance): Length<T> = (this.distance - distance).toLength(unit)

  public operator fun times(multiplicand: Int): Length<T> = (distance * multiplicand).toLength(unit)

  public operator fun times(multiplicand: Long): Length<T> = (distance * multiplicand).toLength(unit)

  public operator fun times(multiplicand: Float): Length<T> = (distance * multiplicand).toLength(unit)

  public operator fun times(multiplicand: Double): Length<T> = (distance * multiplicand).toLength(unit)

  public operator fun div(multiplicand: Int): Length<T> = (distance / multiplicand).toLength(unit)

  public operator fun div(multiplicand: Long): Length<T> = (distance / multiplicand).toLength(unit)

  public operator fun div(multiplicand: Float): Length<T> = (distance / multiplicand).toLength(unit)

  public operator fun div(multiplicand: Double): Length<T> = (distance / multiplicand).toLength(unit)

  public operator fun unaryMinus(): Length<T> = this * -1

  override fun compareTo(other: Length<*>): Int = distance.compareTo(other.distance)

  override fun equals(other: Any?): Boolean = other is Length<*> &&
      distance == other.distance &&
      unit == other.unit

  override fun hashCode(): Int = 31 * distance.hashCode() + unit.hashCode()

  override fun toString(): String = "Length(measure=$measure, unit=$unit)"

  public companion object {
    public fun <T> of(
      value: Long,
      unit: T,
    ): Length<T> where T : Enum<T>, T : LengthUnit<T> = Length(Distance.of(value, unit), unit)

    public fun ofGigameters(value: Long): Length<SiLengthUnit> = of(value, Gigameter)

    public fun ofMegameters(value: Long): Length<SiLengthUnit> = of(value, Megameter)

    public fun ofKilometers(value: Long): Length<SiLengthUnit> = of(value, Kilometer)

    public fun ofMeters(value: Long): Length<SiLengthUnit> = of(value, Meter)

    public fun ofMillimeters(value: Long): Length<SiLengthUnit> = of(value, Millimeter)

    public fun ofMicrometers(value: Long): Length<SiLengthUnit> = of(value, Micrometer)

    public fun ofNanometers(value: Long): Length<SiLengthUnit> = of(value, Nanometer)

    public fun ofMiles(value: Long): Length<ImperialLengthUnit> = of(value, Mile)

    public fun ofYards(value: Long): Length<ImperialLengthUnit> = of(value, Yard)

    public fun ofFeet(value: Long): Length<ImperialLengthUnit> = of(value, Foot)

    public fun ofInches(value: Long): Length<ImperialLengthUnit> = of(value, Inch)

    public fun <T> of(
      value: Double,
      unit: T,
    ): Length<T> where T : Enum<T>, T : LengthUnit<T> = Length(Distance.of(value, unit), unit)

    public fun ofGigameters(value: Double): Length<SiLengthUnit> = of(value, Gigameter)

    public fun ofMegameters(value: Double): Length<SiLengthUnit> = of(value, Megameter)

    public fun ofKilometers(value: Double): Length<SiLengthUnit> = of(value, Kilometer)

    public fun ofMeters(value: Double): Length<SiLengthUnit> = of(value, Meter)

    public fun ofMillimeters(value: Double): Length<SiLengthUnit> = of(value, Millimeter)

    public fun ofMicrometers(value: Double): Length<SiLengthUnit> = of(value, Micrometer)

    public fun ofNanometers(value: Double): Length<SiLengthUnit> = of(value, Nanometer)

    public fun ofMiles(value: Double): Length<ImperialLengthUnit> = of(value, Mile)

    public fun ofYards(value: Double): Length<ImperialLengthUnit> = of(value, Yard)

    public fun ofFeet(value: Double): Length<ImperialLengthUnit> = of(value, Foot)

    public fun ofInches(value: Double): Length<ImperialLengthUnit> = of(value, Inch)
  }
}
