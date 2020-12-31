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
import java.math.BigDecimal
import kotlin.Long.Companion.MAX_VALUE
import kotlin.Long.Companion.MIN_VALUE

/**
 * Meter–based numerical representation of how far apart two points are, such as '150.5 meters'. This class
 * can hold negative values to represent the direction.
 *
 * This class models a quantity of space in terms of meters and nanometers. Physical distance
 * could be of infinite length. For practicality, the can have values between [Long.MIN_VALUE] meters exclusive
 * and [Long.MAX_VALUE] meters and `999_999_999` nanometers inclusive, which is approximately -975 and 975 light years.
 * Exclusive lower bound is set An attempt to create a distance out of these bounds will throw an exception.
 */
public class Distance private constructor(
  private val metersPart: Long = 0L,
  private val nanosPart: Long = 0L,
) : Comparable<Distance> {
  public val meters: BigDecimal =
    (metersPart.toBigDecimal() + (nanosPart.toDouble() / nanosInMeter).toBigDecimal()).setScale(9)

  /**
   * Converts this distance to a [Length] with [unit].
   */
  public fun <T : LengthUnit<T>> toLength(unit: T): Length<T> = Length(this, unit)

  /**
   * Transforms this distance to a [Length] and formats it to a human-readable form. General formatting behaviour
   * is defined in [Ruler].
   *
   * @param converter Conversion rules that should override default ones.
   * @param formatter Formatting rules that should override default ones.
   */
  public fun format(
    converter: LengthConverter? = Ruler,
    formatter: LengthFormatter = Ruler,
  ): String = format(Ruler.driver, converter, formatter)

  /**
   * Transforms this distance to a [Length] and formats it to a human-readable form. General formatting behaviour
   * is defined in [Ruler].
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

  private fun format(
    driver: FormattingDriver,
    converter: LengthConverter? = Ruler,
    formatter: LengthFormatter = Ruler,
  ): String = driver.toLocalizedLength(this).format(driver, converter, formatter)

  /**
   * Returns a distance whose value is the absolute value of this distance.
   */
  public fun abs(): Distance = if (meters >= BigDecimal.ZERO) this else create(meters.abs())

  /**
   * Adds specified distance to this distance.
   */
  public operator fun plus(other: Distance): Distance = create(
      metersPart.safeAdd(other.metersPart),
      nanosPart.safeAdd(other.nanosPart),
  )

  /**
   * Adds specified [Length] to this distance.
   */
  public operator fun plus(length: Length<*>): Distance = this + length.distance

  /**
   * Subtracts specified distance from this distance.
   */
  public operator fun minus(other: Distance): Distance = create(
      metersPart.safeSubtract(other.metersPart),
      nanosPart.safeSubtract(other.nanosPart),
  )

  /**
   * Subtracts specified [Length] from this distance.
   */
  public operator fun minus(length: Length<*>): Distance = this - length.distance

  /**
   * Multiplies this distance by specified value.
   */
  public operator fun times(multiplicand: Int): Distance = this * multiplicand.toLong()

  /**
   * Multiplies this distance by specified value.
   */
  public operator fun times(multiplicand: Long): Distance = when (multiplicand) {
    0L -> Zero
    1L -> this
    else -> create(meters * multiplicand.toBigDecimal())
  }

  /**
   * Multiplies this distance by specified value.
   */
  public operator fun times(multiplicand: Float): Distance = this * multiplicand.toDouble()

  /**
   * Multiplies this distance by specified value.
   */
  public operator fun times(multiplicand: Double): Distance = when (multiplicand) {
    0.0 -> Zero
    1.0 -> this
    else -> create(meters * multiplicand.toBigDecimal())
  }

  /**
   * Divides this distance by specified value.
   */
  public operator fun div(divisor: Int): Distance = this / divisor.toLong()

  /**
   * Divides this distance by specified value.
   */
  public operator fun div(divisor: Long): Distance = when (divisor) {
    0L -> throw ArithmeticException("Cannot divide by 0.")
    1L -> this
    else -> create(meters / divisor.toBigDecimal())
  }

  /**
   * Divides this distance by specified value.
   */
  public operator fun div(divisor: Float): Distance = this / divisor.toDouble()

  /**
   * Divides this distance by specified value.
   */
  public operator fun div(divisor: Double): Distance = when (divisor) {
    0.0 -> throw ArithmeticException("Cannot divide by 0.")
    1.0 -> this
    else -> create(meters / divisor.toBigDecimal())
  }

  /**
   * Negates this distance.
   */
  public operator fun unaryMinus(): Distance = this * -1

  /**
   * Compares this distance based on the space quantity.
   */
  override fun compareTo(other: Distance): Int {
    val cmp = metersPart.compareTo(other.metersPart)
    return if (cmp != 0) cmp else nanosPart.compareTo(other.nanosPart)
  }

  override fun equals(other: Any?): Boolean = other is Distance &&
      metersPart == other.metersPart &&
      nanosPart == other.nanosPart

  override fun hashCode(): Int = 31 * metersPart.hashCode() + nanosPart.hashCode()

  override fun toString(): String = "Distance(meters=${meters.stripTrailingZeros().toPlainString()})"

  public companion object {
    private const val nanosInMeter = 1_000_000_000L
    private val nanosInMeterBig = nanosInMeter.toBigInteger()

    /**
     * Constant for a maximal possible distance to express by this class.
     */
    public val Max: Distance = Distance(MAX_VALUE, nanosInMeter - 1)

    /**
     * Constant for a minimal possible distance to express by this class.
     */
    public val Min: Distance = -Max

    /**
     * Constant for a distance of zero.
     */
    public val Zero: Distance = Distance()

    /**
     * Constant for a smallest grain of a distance.
     */
    public val Epsilon: Distance = Distance(nanosPart = 1)

    internal fun create(meters: BigDecimal): Distance {
      val nanos = meters.movePointRight(9).toBigInteger()
      val divRem = nanos.divideAndRemainder(nanosInMeterBig)
      if (divRem[0].bitLength() > 63) {
        throw ArithmeticException("Exceeded distance capacity: $nanos nm.")
      }
      val storedMeters = divRem[0].toLong()
      val storedNanometers = divRem[1].toLong()
      return create(storedMeters, storedNanometers)
    }

    internal fun create(
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
          .takeIf { it in 0..999_999_999 }
          ?: throw ArithmeticException("Exceeded nanometers capacity: $nanoPart nm")
      if (totalMeters == MIN_VALUE && totalNanometers == 0L) {
        throw ArithmeticException("Exceeded meters capacity: $totalMeters m")
      }

      return Distance(totalMeters, totalNanometers)
    }

    /**
     * Creates a distance representing a value in the specified unit.
     */
    public fun of(value: Long, unit: LengthUnit<*>): Distance = create(
        meters = value.toBigDecimal() * unit.meterRatio,
    )

    /**
     * Creates a distance representing a value expressed in [SiLengthUnit.Gigameter].
     */
    public fun ofGigameters(value: Long): Distance = of(value, Gigameter)

    /**
     * Creates a distance representing a value expressed in [SiLengthUnit.Megameter].
     */
    public fun ofMegameters(value: Long): Distance = of(value, Megameter)

    /**
     * Creates a distance representing a value expressed in [SiLengthUnit.Kilometer].
     */
    public fun ofKilometers(value: Long): Distance = of(value, Kilometer)

    /**
     * Creates a distance representing a value expressed in [SiLengthUnit.Decimeter].
     */
    public fun ofHectometers(value: Long): Distance = of(value, Hectometer)

    /**
     * Creates a distance representing a value expressed in [SiLengthUnit.Centimeter].
     */
    public fun ofDecameters(value: Long): Distance = of(value, Decameter)

    /**
     * Creates a distance representing a value expressed in [SiLengthUnit.Meter].
     */
    public fun ofMeters(value: Long): Distance = of(value, Meter)

    /**
     * Creates a distance representing a value expressed in [SiLengthUnit.Decimeter].
     */
    public fun ofDecimeters(value: Long): Distance = of(value, Decimeter)

    /**
     * Creates a distance representing a value expressed in [SiLengthUnit.Centimeter].
     */
    public fun ofCentimeters(value: Long): Distance = of(value, Centimeter)

    /**
     * Creates a distance representing a value expressed in [SiLengthUnit.Millimeter].
     */
    public fun ofMillimeters(value: Long): Distance = of(value, Millimeter)

    /**
     * Creates a distance representing a value expressed in [SiLengthUnit.Micrometer].
     */
    public fun ofMicrometers(value: Long): Distance = of(value, Micrometer)

    /**
     * Creates a distance representing a value expressed in [SiLengthUnit.Nanometer].
     */
    public fun ofNanometers(value: Long): Distance = of(value, Nanometer)

    /**
     * Creates a distance representing a value expressed in [ImperialLengthUnit.Mile].
     */
    public fun ofMiles(value: Long): Distance = of(value, Mile)

    /**
     * Creates a distance representing a value expressed in [ImperialLengthUnit.Yard].
     */
    public fun ofYards(value: Long): Distance = of(value, Yard)

    /**
     * Creates a distance representing a value expressed in [ImperialLengthUnit.Foot].
     */
    public fun ofFeet(value: Long): Distance = of(value, Foot)

    /**
     * Creates a distance representing a value expressed in [ImperialLengthUnit.Inch].
     */
    public fun ofInches(value: Long): Distance = of(value, Inch)

    /**
     * Creates a distance representing a value in the specified unit.
     */
    public fun of(value: Double, unit: LengthUnit<*>): Distance = create(
        meters = value.toBigDecimal() * unit.meterRatio,
    )

    /**
     * Creates a distance representing a value expressed in [SiLengthUnit.Gigameter].
     */
    public fun ofGigameters(value: Double): Distance = of(value, Gigameter)

    /**
     * Creates a distance representing a value expressed in [SiLengthUnit.Megameter].
     */
    public fun ofMegameters(value: Double): Distance = of(value, Megameter)

    /**
     * Creates a distance representing a value expressed in [SiLengthUnit.Kilometer].
     */
    public fun ofKilometers(value: Double): Distance = of(value, Kilometer)

    /**
     * Creates a distance representing a value expressed in [SiLengthUnit.Decimeter].
     */
    public fun ofHectometers(value: Double): Distance = of(value, Hectometer)

    /**
     * Creates a distance representing a value expressed in [SiLengthUnit.Centimeter].
     */
    public fun ofDecameters(value: Double): Distance = of(value, Decameter)

    /**
     * Creates a distance representing a value expressed in [SiLengthUnit.Meter].
     */
    public fun ofMeters(value: Double): Distance = of(value, Meter)

    /**
     * Creates a distance representing a value expressed in [SiLengthUnit.Decimeter].
     */
    public fun ofDecimeters(value: Double): Distance = of(value, Decimeter)

    /**
     * Creates a distance representing a value expressed in [SiLengthUnit.Centimeter].
     */
    public fun ofCentimeters(value: Double): Distance = of(value, Centimeter)

    /**
     * Creates a distance representing a value expressed in [SiLengthUnit.Millimeter].
     */
    public fun ofMillimeters(value: Double): Distance = of(value, Millimeter)

    /**
     * Creates a distance representing a value expressed in [SiLengthUnit.Micrometer].
     */
    public fun ofMicrometers(value: Double): Distance = of(value, Micrometer)

    /**
     * Creates a distance representing a value expressed in [SiLengthUnit.Nanometer].
     */
    public fun ofNanometers(value: Double): Distance = of(value, Nanometer)

    /**
     * Creates a distance representing a value expressed in [ImperialLengthUnit.Mile].
     */
    public fun ofMiles(value: Double): Distance = of(value, Mile)

    /**
     * Creates a distance representing a value expressed in [ImperialLengthUnit.Yard].
     */
    public fun ofYards(value: Double): Distance = of(value, Yard)

    /**
     * Creates a distance representing a value expressed in [ImperialLengthUnit.Foot].
     */
    public fun ofFeet(value: Double): Distance = of(value, Foot)

    /**
     * Creates a distance representing a value expressed in [ImperialLengthUnit.Inch].
     */
    public fun ofInches(value: Double): Distance = of(value, Inch)
  }
}
