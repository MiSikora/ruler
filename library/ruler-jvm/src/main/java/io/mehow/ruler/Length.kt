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

/**
 * A representation of [Distance] with a dimensional unit.
 */
public class Length<T : LengthUnit<T>> internal constructor(
  /**
   * A distance that is used for measurement of this length.
   */
  public val distance: Distance,
  /**
   * A dimensional unit that is used for measuring a distance in this length.
   */
  public val unit: T,
) : Comparable<Length<*>> {
  /**
   * An amount of units that fit in a [distance].
   */
  public val measure: BigDecimal = distance.meters.divide(unit.meterRatio, 9, DOWN)

  /**
   * Applies provided unit to this length.
   */
  @Suppress("UNCHECKED_CAST")
  public fun <R : LengthUnit<R>> withUnit(unit: R): Length<R> = when (this.unit) {
    unit -> this as Length<R>
    else -> Length(distance, unit)
  }

  /**
   * Applies a best fitted unit to this length from all available units in a unit system.
   */
  @Deprecated(
      message = "Will be removed in a next major release.",
      replaceWith = ReplaceWith("withFittingUnit()"),
  )
  public fun withAutoUnit(): Length<T> = withFittingUnit()

  /**
   * Applies a unit to this length from specified units according to the fitting algorithm.
   */
  public fun withFittingUnit(
    units: Iterable<T> = unit.units,
    unitFitter: UnitFitter = BuiltInUnitFitter,
  ): Length<T> = unitFitter.findFit(units, this)?.let(::withUnit) ?: this

  /**
   * Ensures that a unit is within a specified range.
   */
  public fun coerceUnitIn(range: ClosedRange<T>): Length<T> = when {
    range.isEmpty() -> throw IllegalArgumentException("Range cannot be empty!")
    unit > range.endInclusive -> Length(distance, range.endInclusive)
    unit < range.start -> Length(distance, range.start)
    else -> this
  }

  /**
   * Ensures that a unit is between provided min and max values.
   */
  public fun coerceUnitIn(min: T, max: T): Length<T> = coerceUnitIn(min..max)

  /**
   * Ensures that a unit is not lesser than the provided value.
   */
  public fun coerceUnitAtLeastTo(min: T): Length<T> = when {
    unit < min -> Length(distance, min)
    else -> this
  }

  /**
   * Ensures that a unit is not greater than the provided value.
   */
  public fun coerceUnitAtMostTo(max: T): Length<T> = when {
    unit > max -> Length(distance, max)
    else -> this
  }

  /**
   * Returns a length whose value is the absolute value of this length.
   */
  public fun abs(): Length<T> = Length(distance.abs(), unit)

  /**
   * Adds specified length to this length. Left hand side unit is preserved.
   */
  public operator fun plus(other: Length<*>): Length<T> = (distance + other.distance).toLength(unit)

  /**
   * Adds specified distance to this length.
   */
  public operator fun plus(distance: Distance): Length<T> = (this.distance + distance).toLength(unit)

  /**
   * Subtracts specified length from this length. Left hand side unit is preserved.
   */
  public operator fun minus(other: Length<*>): Length<T> = (distance - other.distance).toLength(unit)

  /**
   * Subtracts specified distance from this length.
   */
  public operator fun minus(distance: Distance): Length<T> = (this.distance - distance).toLength(unit)

  /**
   * Multiplies this length by specified value.
   */
  public operator fun times(multiplicand: Int): Length<T> = (distance * multiplicand).toLength(unit)

  /**
   * Multiplies this length by specified value.
   */
  public operator fun times(multiplicand: Long): Length<T> = (distance * multiplicand).toLength(unit)

  /**
   * Multiplies this length by specified value.
   */
  public operator fun times(multiplicand: Float): Length<T> = (distance * multiplicand).toLength(unit)

  /**
   * Multiplies this length by specified value.
   */
  public operator fun times(multiplicand: Double): Length<T> = (distance * multiplicand).toLength(unit)

  /**
   * Divides this length by specified value.
   */
  public operator fun div(divisor: Int): Length<T> = (distance / divisor).toLength(unit)

  /**
   * Divides this length by specified value.
   */
  public operator fun div(divisor: Long): Length<T> = (distance / divisor).toLength(unit)

  /**
   * Divides this length by specified value.
   */
  public operator fun div(divisor: Float): Length<T> = (distance / divisor).toLength(unit)

  /**
   * Divides this length by specified value.
   */
  public operator fun div(divisor: Double): Length<T> = (distance / divisor).toLength(unit)

  /**
   * Negates this length.
   */
  public operator fun unaryMinus(): Length<T> = this * -1

  /**
   * Compares this distance based on the distance.
   */
  override fun compareTo(other: Length<*>): Int = distance.compareTo(other.distance)

  override fun equals(other: Any?): Boolean = other is Length<*> &&
      distance == other.distance &&
      unit == other.unit

  override fun hashCode(): Int = 31 * distance.hashCode() + unit.hashCode()

  override fun toString(): String = "Length(measure=${measure.stripTrailingZeros().toPlainString()}, unit=$unit)"

  public companion object {

    /**
     * Creates a length representing a value in the specified unit.
     */
    public fun <T : LengthUnit<T>> of(value: Long, unit: T): Length<T> = Length(Distance.of(value, unit), unit)

    /**
     * Creates a distance representing a value expressed in [SiLengthUnit.Gigameter].
     */
    public fun ofGigameters(value: Long): Length<SiLengthUnit> = of(value, Gigameter)

    /**
     * Creates a distance representing a value expressed in [SiLengthUnit.Megameter].
     */
    public fun ofMegameters(value: Long): Length<SiLengthUnit> = of(value, Megameter)

    /**
     * Creates a distance representing a value expressed in [SiLengthUnit.Kilometer].
     */
    public fun ofKilometers(value: Long): Length<SiLengthUnit> = of(value, Kilometer)

    /**
     * Creates a distance representing a value expressed in [SiLengthUnit.Meter].
     */
    public fun ofMeters(value: Long): Length<SiLengthUnit> = of(value, Meter)

    /**
     * Creates a distance representing a value expressed in [SiLengthUnit.Millimeter].
     */
    public fun ofMillimeters(value: Long): Length<SiLengthUnit> = of(value, Millimeter)

    /**
     * Creates a distance representing a value expressed in [SiLengthUnit.Micrometer].
     */
    public fun ofMicrometers(value: Long): Length<SiLengthUnit> = of(value, Micrometer)

    /**
     * Creates a distance representing a value expressed in [SiLengthUnit.Nanometer].
     */
    public fun ofNanometers(value: Long): Length<SiLengthUnit> = of(value, Nanometer)

    /**
     * Creates a distance representing a value expressed in [ImperialLengthUnit.Mile].
     */
    public fun ofMiles(value: Long): Length<ImperialLengthUnit> = of(value, Mile)

    /**
     * Creates a distance representing a value expressed in [ImperialLengthUnit.Yard].
     */
    public fun ofYards(value: Long): Length<ImperialLengthUnit> = of(value, Yard)

    /**
     * Creates a distance representing a value expressed in [ImperialLengthUnit.Foot].
     */
    public fun ofFeet(value: Long): Length<ImperialLengthUnit> = of(value, Foot)

    /**
     * Creates a distance representing a value expressed in [ImperialLengthUnit.Inch].
     */
    public fun ofInches(value: Long): Length<ImperialLengthUnit> = of(value, Inch)

    /**
     * Creates a length representing a value in the specified unit.
     */
    public fun <T : LengthUnit<T>> of(value: Double, unit: T): Length<T> = Length(Distance.of(value, unit), unit)

    /**
     * Creates a distance representing a value expressed in [SiLengthUnit.Gigameter].
     */
    public fun ofGigameters(value: Double): Length<SiLengthUnit> = of(value, Gigameter)

    /**
     * Creates a distance representing a value expressed in [SiLengthUnit.Megameter].
     */
    public fun ofMegameters(value: Double): Length<SiLengthUnit> = of(value, Megameter)

    /**
     * Creates a distance representing a value expressed in [SiLengthUnit.Kilometer].
     */
    public fun ofKilometers(value: Double): Length<SiLengthUnit> = of(value, Kilometer)

    /**
     * Creates a distance representing a value expressed in [SiLengthUnit.Meter].
     */
    public fun ofMeters(value: Double): Length<SiLengthUnit> = of(value, Meter)

    /**
     * Creates a distance representing a value expressed in [SiLengthUnit.Millimeter].
     */
    public fun ofMillimeters(value: Double): Length<SiLengthUnit> = of(value, Millimeter)

    /**
     * Creates a distance representing a value expressed in [SiLengthUnit.Micrometer].
     */
    public fun ofMicrometers(value: Double): Length<SiLengthUnit> = of(value, Micrometer)

    /**
     * Creates a distance representing a value expressed in [SiLengthUnit.Nanometer].
     */
    public fun ofNanometers(value: Double): Length<SiLengthUnit> = of(value, Nanometer)

    /**
     * Creates a distance representing a value expressed in [ImperialLengthUnit.Mile].
     */
    public fun ofMiles(value: Double): Length<ImperialLengthUnit> = of(value, Mile)

    /**
     * Creates a distance representing a value expressed in [ImperialLengthUnit.Yard].
     */
    public fun ofYards(value: Double): Length<ImperialLengthUnit> = of(value, Yard)

    /**
     * Creates a distance representing a value expressed in [ImperialLengthUnit.Foot].
     */
    public fun ofFeet(value: Double): Length<ImperialLengthUnit> = of(value, Foot)

    /**
     * Creates a distance representing a value expressed in [ImperialLengthUnit.Inch].
     */
    public fun ofInches(value: Double): Length<ImperialLengthUnit> = of(value, Inch)
  }
}
