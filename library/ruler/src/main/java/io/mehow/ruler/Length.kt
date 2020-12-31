package io.mehow.ruler

import io.mehow.ruler.ImperialLengthUnit.Foot
import io.mehow.ruler.ImperialLengthUnit.Inch
import io.mehow.ruler.ImperialLengthUnit.Mile
import io.mehow.ruler.ImperialLengthUnit.Yard
import io.mehow.ruler.SiLengthUnit.Centimeter
import io.mehow.ruler.SiLengthUnit.Decameter
import io.mehow.ruler.SiLengthUnit.Decimeter
import io.mehow.ruler.SiLengthUnit.Gigameter
import io.mehow.ruler.SiLengthUnit.Hectometer
import io.mehow.ruler.SiLengthUnit.Kilometer
import io.mehow.ruler.SiLengthUnit.Megameter
import io.mehow.ruler.SiLengthUnit.Meter
import io.mehow.ruler.SiLengthUnit.Micrometer
import io.mehow.ruler.SiLengthUnit.Millimeter
import io.mehow.ruler.SiLengthUnit.Nanometer
import io.mehow.ruler.format.FormattingContext
import io.mehow.ruler.format.FormattingDriver
import io.mehow.ruler.format.LengthConverter
import io.mehow.ruler.format.LengthFormatter

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
  public val measure: Measure<T> = Measure.lengthMeasure(this)

  /**
   * Applies provided unit to this length.
   */
  @Suppress("UNCHECKED_CAST")
  public fun <R : LengthUnit<R>> withUnit(unit: R): Length<R> = when (this.unit) {
    unit -> this as Length<R>
    else -> Length(distance, unit)
  }

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
   * Formats this length to a human-readable form. General formatting behaviour is defined in [Ruler].
   *
   * @param converter Conversion rules that should override default ones.
   * @param formatter Formatting rules that should override default ones.
   */
  public fun format(
    converter: LengthConverter? = Ruler,
    formatter: LengthFormatter = Ruler,
  ): String = format(Ruler.driver, converter, formatter)

  /**
   * Formats this length to a human-readable form. General formatting behaviour is defined in [Ruler].
   *
   * @param context Formatting properties that should override default ones.
   * @param converter Conversion rules that should override default ones.
   * @param formatter Formatting rules that should override default ones.
   */
  public fun format(
    context: FormattingContext,
    converter: LengthConverter? = Ruler,
    formatter: LengthFormatter = Ruler,
  ): String = format(
      driver = Ruler.driver.newBuilder().withFormattingContext(context).build(),
      converter = converter,
      formatter = formatter,
  )

  internal fun format(
    driver: FormattingDriver,
    converter: LengthConverter?,
    formatter: LengthFormatter,
  ): String {
    val length = converter?.convert(this) ?: this
    return formatter.format(length, driver)
  }

  /**
   * Returns a length whose value is the absolute value of this length.
   */
  public fun abs(): Length<T> = if (distance >= Distance.Zero) this else Length(distance.abs(), unit)

  /**
   * Returns a length whose [measure] is rounded to a whole number that is closest towards 0.
   */
  public fun roundDown(): Length<T> = of(measure.value.toLong(), unit)

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

  override fun toString(): String = "Length(measure=$measure)"

  public companion object {

    /**
     * Creates a length representing a value in the specified unit.
     */
    public fun <T : LengthUnit<T>> of(value: Long, unit: T): Length<T> = Length(Distance.of(value, unit), unit)

    /**
     * Creates a length representing a value expressed in [SiLengthUnit.Gigameter].
     */
    public fun ofGigameters(value: Long): Length<SiLengthUnit> = of(value, Gigameter)

    /**
     * Creates a length representing a value expressed in [SiLengthUnit.Megameter].
     */
    public fun ofMegameters(value: Long): Length<SiLengthUnit> = of(value, Megameter)

    /**
     * Creates a length representing a value expressed in [SiLengthUnit.Kilometer].
     */
    public fun ofKilometers(value: Long): Length<SiLengthUnit> = of(value, Kilometer)

    /**
     * Creates a length representing a value expressed in [SiLengthUnit.Decimeter].
     */
    public fun ofHectometers(value: Long): Length<SiLengthUnit> = of(value, Hectometer)

    /**
     * Creates a length representing a value expressed in [SiLengthUnit.Centimeter].
     */
    public fun ofDecameters(value: Long): Length<SiLengthUnit> = of(value, Decameter)

    /**
     * Creates a length representing a value expressed in [SiLengthUnit.Meter].
     */
    public fun ofMeters(value: Long): Length<SiLengthUnit> = of(value, Meter)

    /**
     * Creates a length representing a value expressed in [SiLengthUnit.Decimeter].
     */
    public fun ofDecimeters(value: Long): Length<SiLengthUnit> = of(value, Decimeter)

    /**
     * Creates a length representing a value expressed in [SiLengthUnit.Centimeter].
     */
    public fun ofCentimeters(value: Long): Length<SiLengthUnit> = of(value, Centimeter)

    /**
     * Creates a length representing a value expressed in [SiLengthUnit.Millimeter].
     */
    public fun ofMillimeters(value: Long): Length<SiLengthUnit> = of(value, Millimeter)

    /**
     * Creates a length representing a value expressed in [SiLengthUnit.Micrometer].
     */
    public fun ofMicrometers(value: Long): Length<SiLengthUnit> = of(value, Micrometer)

    /**
     * Creates a length representing a value expressed in [SiLengthUnit.Nanometer].
     */
    public fun ofNanometers(value: Long): Length<SiLengthUnit> = of(value, Nanometer)

    /**
     * Creates a length representing a value expressed in [ImperialLengthUnit.Mile].
     */
    public fun ofMiles(value: Long): Length<ImperialLengthUnit> = of(value, Mile)

    /**
     * Creates a length representing a value expressed in [ImperialLengthUnit.Yard].
     */
    public fun ofYards(value: Long): Length<ImperialLengthUnit> = of(value, Yard)

    /**
     * Creates a length representing a value expressed in [ImperialLengthUnit.Foot].
     */
    public fun ofFeet(value: Long): Length<ImperialLengthUnit> = of(value, Foot)

    /**
     * Creates a length representing a value expressed in [ImperialLengthUnit.Inch].
     */
    public fun ofInches(value: Long): Length<ImperialLengthUnit> = of(value, Inch)

    /**
     * Creates a length representing a value in the specified unit.
     */
    public fun <T : LengthUnit<T>> of(value: Double, unit: T): Length<T> = Length(Distance.of(value, unit), unit)

    /**
     * Creates a length representing a value expressed in [SiLengthUnit.Gigameter].
     */
    public fun ofGigameters(value: Double): Length<SiLengthUnit> = of(value, Gigameter)

    /**
     * Creates a length representing a value expressed in [SiLengthUnit.Megameter].
     */
    public fun ofMegameters(value: Double): Length<SiLengthUnit> = of(value, Megameter)

    /**
     * Creates a length representing a value expressed in [SiLengthUnit.Kilometer].
     */
    public fun ofKilometers(value: Double): Length<SiLengthUnit> = of(value, Kilometer)

    /**
     * Creates a length representing a value expressed in [SiLengthUnit.Decimeter].
     */
    public fun ofHectometers(value: Double): Length<SiLengthUnit> = of(value, Hectometer)

    /**
     * Creates a length representing a value expressed in [SiLengthUnit.Centimeter].
     */
    public fun ofDecameters(value: Double): Length<SiLengthUnit> = of(value, Decameter)

    /**
     * Creates a length representing a value expressed in [SiLengthUnit.Meter].
     */
    public fun ofMeters(value: Double): Length<SiLengthUnit> = of(value, Meter)

    /**
     * Creates a length representing a value expressed in [SiLengthUnit.Decimeter].
     */
    public fun ofDecimeters(value: Double): Length<SiLengthUnit> = of(value, Decimeter)

    /**
     * Creates a length representing a value expressed in [SiLengthUnit.Centimeter].
     */
    public fun ofCentimeters(value: Double): Length<SiLengthUnit> = of(value, Centimeter)

    /**
     * Creates a length representing a value expressed in [SiLengthUnit.Millimeter].
     */
    public fun ofMillimeters(value: Double): Length<SiLengthUnit> = of(value, Millimeter)

    /**
     * Creates a length representing a value expressed in [SiLengthUnit.Micrometer].
     */
    public fun ofMicrometers(value: Double): Length<SiLengthUnit> = of(value, Micrometer)

    /**
     * Creates a length representing a value expressed in [SiLengthUnit.Nanometer].
     */
    public fun ofNanometers(value: Double): Length<SiLengthUnit> = of(value, Nanometer)

    /**
     * Creates a length representing a value expressed in [ImperialLengthUnit.Mile].
     */
    public fun ofMiles(value: Double): Length<ImperialLengthUnit> = of(value, Mile)

    /**
     * Creates a length representing a value expressed in [ImperialLengthUnit.Yard].
     */
    public fun ofYards(value: Double): Length<ImperialLengthUnit> = of(value, Yard)

    /**
     * Creates a length representing a value expressed in [ImperialLengthUnit.Foot].
     */
    public fun ofFeet(value: Double): Length<ImperialLengthUnit> = of(value, Foot)

    /**
     * Creates a length representing a value expressed in [ImperialLengthUnit.Inch].
     */
    public fun ofInches(value: Double): Length<ImperialLengthUnit> = of(value, Inch)
  }
}
