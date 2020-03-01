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

/**
 * A numerical representation of physical distance, such as '25 meters'.
 *
 * This class models a quantity of distance in terms of meters and nanometers. It can be converted
 * to other distance-based units, such as kilometers or miles.
 *
 * For practicality, the distance is stored in two parts; [metersPart] and [nanosPart]. The distance
 * restricts meters part between [Long.MIN_VALUE] and [Long.MAX_VALUE]
 * and uses nanometers resolution with a maximum value of meters that can be held in a [Long].
 * This limits objects of this class to hold values in the range of `(-974.91, 974.91)` light years.
 *
 * This class is immutable and thread safe.
 */
class Distance private constructor(
  /**
   * Amount of meters in this [Distance]. Can be negative.
   */
  val metersPart: Long = 0L,
  /**
   * Amount of nanometers in this [Distance]. Value is always between `0` and `999,999,999`.
   * This means that `-1 nanometer` is represented as `-1 meter and 999,999,999` nanometers.
   */
  val nanosPart: Long = 0L
) : Comparable<Distance> {
  /**
   * Exact value of meters in this [Distance] that takes into account negative values
   * and nanometers part.
   */
  val exactTotalMeters = metersPart.toBigDecimal() +
      (nanosPart.toDouble() / nanosInMeter).toBigDecimal()

  /**
   * Converts this [Distance] into a [Length] of [LengthUnit].
   *
   * @param T [LengthUnit] with which returned [Length] should be associated.
   */
  fun <T> toLength(unit: T): Length<T> where T : Enum<T>, T : LengthUnit<T> {
    return Length(this, unit)
  }

  /**
   * Returns a copy of this [Distance] with the specified other [Distance] added.
   *
   * @param [other] [Distance] to add, positive or negative.
   * @throws ArithmeticException If numeric overflow occurs.
   */
  operator fun plus(other: Distance): Distance {
    return create(
        metersPart.safeAdd(other.metersPart),
        nanosPart.safeAdd(other.nanosPart)
    )
  }

  /**
   * Returns a copy of this [Distance] with the specified [Length] added.
   *
   * @param [length] [Length] to add, positive or negative.
   * @throws ArithmeticException If numeric overflow occurs.
   */
  operator fun plus(length: Length<*>): Distance {
    return this + length.distance
  }

  /**
   * Returns a copy of this [Distance] with the specified other [Distance] subtracted.
   *
   * @param [other] [Distance] to subtract, positive or negative.
   * @throws ArithmeticException If numeric overflow occurs.
   */
  operator fun minus(other: Distance): Distance {
    return create(
        metersPart.safeSubtract(other.metersPart),
        nanosPart.safeSubtract(other.nanosPart)
    )
  }

  /**
   * Returns a copy of this [Distance] with the specified [Length] subtracted.
   *
   * @param [length] [Length] to subtract, positive or negative.
   * @throws ArithmeticException If numeric overflow occurs.
   */
  operator fun minus(length: Length<*>): Distance {
    return this - length.distance
  }

  /**
   * Returns a copy of this [Distance] multiplied by a specified value.
   *
   * @param [multiplicand] [Int] to multiply this [Distance] with, positive or negative.
   * @throws ArithmeticException If numeric overflow occurs.
   */
  operator fun times(multiplicand: Int): Distance {
    return this * multiplicand.toLong()
  }

  /**
   * Returns a copy of this [Distance] multiplied by a specified value.
   *
   * @param [multiplicand] [Long] to multiply this [Distance] with, positive or negative.
   * @throws ArithmeticException If numeric overflow occurs.
   */
  operator fun times(multiplicand: Long): Distance {
    return when (multiplicand) {
      0L -> zero
      1L -> this
      else -> create(exactTotalMeters * multiplicand.toBigDecimal())
    }
  }

  /**
   * Returns a copy of this [Distance] multiplied by a specified value.
   *
   * @param [multiplicand] [Float] to multiply this [Distance] with, positive or negative.
   * @throws ArithmeticException If numeric overflow occurs.
   */
  operator fun times(multiplicand: Float): Distance {
    return this * multiplicand.toDouble()
  }

  /**
   * Returns a copy of this [Distance] multiplied by a specified value.
   *
   * @param [multiplicand] [Double] to multiply this [Distance] with, positive or negative.
   * @throws ArithmeticException If numeric overflow occurs.
   */
  operator fun times(multiplicand: Double): Distance {
    return when (multiplicand) {
      0.0 -> zero
      1.0 -> this
      else -> create(exactTotalMeters * multiplicand.toBigDecimal())
    }
  }

  /**
   * Returns a copy of this [Distance] divided by a specified value.
   *
   * @param [divisor] [Int] to divide this [Distance] with, positive or negative.
   * @throws ArithmeticException If numeric overflow occurs.
   * @throws IllegalArgumentException If [divisor] is zero.
   */
  operator fun div(divisor: Int): Distance {
    return this / divisor.toLong()
  }

  /**
   * Returns a copy of this [Distance] divided by a specified value.
   *
   * @param [divisor] [Long] to divide this [Distance] with, positive or negative.
   * @throws ArithmeticException If numeric overflow occurs.
   * @throws IllegalArgumentException If [divisor] is zero.
   */
  operator fun div(divisor: Long): Distance {
    require(divisor != 0L) { "Cannot divide by 0." }

    return if (divisor == 1L) this
    else create(exactTotalMeters.divide(divisor.toBigDecimal(), DOWN))
  }

  /**
   * Returns a copy of this [Distance] divided by a specified value.
   *
   * @param [divisor] [Float] to divide this [Distance] with, positive or negative.
   * @throws ArithmeticException If numeric overflow occurs.
   * @throws IllegalArgumentException If [divisor] is zero.
   */
  operator fun div(divisor: Float): Distance {
    return this / divisor.toDouble()
  }

  /**
   * Returns a copy of this [Distance] divided by a specified value.
   *
   * @param [divisor] [Double] to divide this [Distance] with, positive or negative.
   * @throws ArithmeticException If numeric overflow occurs.
   * @throws IllegalArgumentException If [divisor] is zero.
   */
  operator fun div(divisor: Double): Distance {
    require(divisor != 0.0) { "Cannot divide by 0" }

    return if (divisor == 1.0) this
    else create(exactTotalMeters.divide(divisor.toBigDecimal(), DOWN))
  }

  /**
   * Returns a copy of this [Distance] with a negative value.
   *
   * @throws ArithmeticException If numeric overflow occurs.
   */
  operator fun unaryMinus(): Distance {
    return this * -1
  }

  /**
   * Compares this [Distance] to the specified other [Distance].
   *
   * The comparison is based on the total length of distances. It is "consistent with equals",
   * as defined by [Comparable].
   */
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

    /**
     * Smallest value that can be held by [Distance]. It is roughly equal to `-974.91` light years.
     */
    @JvmStatic val min = Distance(MIN_VALUE, 0L)

    /**
     * Value of [Distance] that is equal to `0` meters.
     */
    @JvmStatic val zero = Distance()

    /**
     * Largest value that can be held by [Distance]. It is roughly equal to `974.91` light years.
     */
    @JvmStatic val max = Distance(MAX_VALUE, nanosInMeter - 1)

    /**
     * Creates a [Distance] with specified meters and nanometers. Any decimal part that is below
     * 9 significant digits is ignored.
     *
     * @param value Amount of meters and nanometers that should be held by [Distance], positive or negative.
     * @throws ArithmeticException If numeric overflow occurs.
     */
    @JvmStatic fun create(value: BigDecimal): Distance {
      val nanos = value.movePointRight(9).toBigInteger()
      val divRem = nanos.divideAndRemainder(bigNanosInMeter)
      if (divRem[0].bitLength() > 63) {
        throw ArithmeticException("Exceeded distance capacity: $nanos")
      }
      val storedMeters = divRem[0].toLong()
      val storedNanometers = divRem[1].toLong()
      return create(storedMeters, storedNanometers)
    }

    /**
     * Creates a [Distance] with specified meters and nanometers.
     *
     * @param meters Amount of meters that should be held by [Distance], positive or negative.
     * @param nanometers Amount of nanometers that should be held by [Distance], positive or negative.
     * @throws ArithmeticException If numeric overflow occurs.
     */
    @JvmStatic fun create(
      meters: Long = 0,
      nanometers: Long = 0
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

    /**
     * Obtains a [Distance] representing a number of provided length units.
     *
     * The meters and nanometers are calculated based on conversion rate provided by the [unit].
     *
     * @param value The amount of the [Distance] measured in terms of the [unit], positive or negative.
     * @param unit The unit that the [Distance] is measured in.
     * @throws ArithmeticException If a numeric overflow occurs.
     */
    @JvmStatic fun of(value: Long, unit: LengthUnit<*>) = unit.toDistance(value)

    /**
     * Obtains a [Distance] representing a number of provided gigameters
     *
     * The meters and nanometers are extracted form the specified gigameters.
     *
     * @param value The amount of gigameters, positive or negative.
     * @throws ArithmeticException If a numeric overflow occurs.
     */
    @JvmStatic fun ofGigameters(value: Long) = of(value, Gigameter)

    /**
     * Obtains a [Distance] representing a number of provided megameters
     *
     * The meters and nanometers are extracted form the specified megameters.
     *
     * @param value The amount of megameters, positive or negative.
     * @throws ArithmeticException If a numeric overflow occurs.
     */
    @JvmStatic fun ofMegameters(value: Long) = of(value, Megameter)

    /**
     * Obtains a [Distance] representing a number of provided kilometers
     *
     * The meters and nanometers are extracted form the specified kilometers.
     *
     * @param value The amount of kilometers, positive or negative.
     * @throws ArithmeticException If a numeric overflow occurs.
     */
    @JvmStatic fun ofKilometers(value: Long) = of(value, Kilometer)

    /**
     * Obtains a [Distance] representing a number of provided meters
     *
     * The meters and nanometers are extracted form the specified meters.
     *
     * @param value The amount of meters, positive or negative.
     * @throws ArithmeticException If a numeric overflow occurs.
     */
    @JvmStatic fun ofMeters(value: Long) = of(value, Meter)

    /**
     * Obtains a [Distance] representing a number of provided millimeters
     *
     * The meters and nanometers are extracted form the specified millimeters.
     *
     * @param value The amount of millimeters, positive or negative.
     * @throws ArithmeticException If a numeric overflow occurs.
     */
    @JvmStatic fun ofMillimeters(value: Long) = of(value, Millimeter)

    /**
     * Obtains a [Distance] representing a number of provided micrometers
     *
     * The meters and nanometers are extracted form the specified micrometers.
     *
     * @param value The amount of micrometers, positive or negative.
     * @throws ArithmeticException If a numeric overflow occurs.
     */
    @JvmStatic fun ofMicrometers(value: Long) = of(value, Micrometer)

    /**
     * Obtains a [Distance] representing a number of provided nanometers
     *
     * The meters and nanometers are extracted form the specified nanometers.
     *
     * @param value The amount of nanometers, positive or negative.
     * @throws ArithmeticException If a numeric overflow occurs.
     */
    @JvmStatic fun ofNanometers(value: Long) = of(value, Nanometer)

    /**
     * Obtains a [Distance] representing a number of provided miles
     *
     * The meters and nanometers are extracted form the specified miles.
     *
     * @param value The amount of miles, positive or negative.
     * @throws ArithmeticException If a numeric overflow occurs.
     */
    @JvmStatic fun ofMiles(value: Long) = of(value, Mile)

    /**
     * Obtains a [Distance] representing a number of provided yards
     *
     * The meters and nanometers are extracted form the specified yards.
     *
     * @param value The amount of yards, positive or negative.
     * @throws ArithmeticException If a numeric overflow occurs.
     */
    @JvmStatic fun ofYards(value: Long) = of(value, Yard)

    /**
     * Obtains a [Distance] representing a number of provided feet
     *
     * The meters and nanometers are extracted form the specified feet.
     *
     * @param value The amount of feet, positive or negative.
     * @throws ArithmeticException If a numeric overflow occurs.
     */
    @JvmStatic fun ofFeet(value: Long) = of(value, Foot)

    /**
     * Obtains a [Distance] representing a number of provided inches
     *
     * The meters and nanometers are extracted form the specified inches.
     *
     * @param value The amount of inches, positive or negative.
     * @throws ArithmeticException If a numeric overflow occurs.
     */
    @JvmStatic fun ofInches(value: Long) = of(value, Inch)

    /**
     * Obtains a [Distance] representing a number of provided length units.
     *
     * The meters and nanometers are calculated based on conversion rate provided by the [unit].
     *
     * @param value The amount of the [Distance] measured in terms of the [unit], positive or negative.
     * @param unit The unit that the [Distance] is measured in.
     * @throws ArithmeticException If a numeric overflow occurs.
     */
    @JvmStatic fun of(value: Double, unit: LengthUnit<*>) = unit.toDistance(value)

    /**
     * Obtains a [Distance] representing a number of provided gigameters
     *
     * The meters and nanometers are extracted form the specified gigameters.
     *
     * @param value The amount of gigameters, positive or negative.
     * @throws ArithmeticException If a numeric overflow occurs.
     */
    @JvmStatic fun ofGigameters(value: Double) = of(value, Gigameter)

    /**
     * Obtains a [Distance] representing a number of provided megameters
     *
     * The meters and nanometers are extracted form the specified megameters.
     *
     * @param value The amount of megameters, positive or negative.
     * @throws ArithmeticException If a numeric overflow occurs.
     */
    @JvmStatic fun ofMegameters(value: Double) = of(value, Megameter)

    /**
     * Obtains a [Distance] representing a number of provided kilometers
     *
     * The meters and nanometers are extracted form the specified kilometers.
     *
     * @param value The amount of kilometers, positive or negative.
     * @throws ArithmeticException If a numeric overflow occurs.
     */
    @JvmStatic fun ofKilometers(value: Double) = of(value, Kilometer)

    /**
     * Obtains a [Distance] representing a number of provided meters
     *
     * The meters and nanometers are extracted form the specified meters.
     *
     * @param value The amount of meters, positive or negative.
     * @throws ArithmeticException If a numeric overflow occurs.
     */
    @JvmStatic fun ofMeters(value: Double) = of(value, Meter)

    /**
     * Obtains a [Distance] representing a number of provided millimeters
     *
     * The meters and nanometers are extracted form the specified millimeters.
     *
     * @param value The amount of millimeters, positive or negative.
     * @throws ArithmeticException If a numeric overflow occurs.
     */
    @JvmStatic fun ofMillimeters(value: Double) = of(value, Millimeter)

    /**
     * Obtains a [Distance] representing a number of provided micrometers
     *
     * The meters and nanometers are extracted form the specified micrometers.
     *
     * @param value The amount of micrometers, positive or negative.
     * @throws ArithmeticException If a numeric overflow occurs.
     */
    @JvmStatic fun ofMicrometers(value: Double) = of(value, Micrometer)

    /**
     * Obtains a [Distance] representing a number of provided nanometers
     *
     * The meters and nanometers are extracted form the specified nanometers.
     *
     * @param value The amount of nanometers, positive or negative.
     * @throws ArithmeticException If a numeric overflow occurs.
     */
    @JvmStatic fun ofNanometers(value: Double) = of(value, Nanometer)

    /**
     * Obtains a [Distance] representing a number of provided miles
     *
     * The meters and nanometers are extracted form the specified miles.
     *
     * @param value The amount of miles, positive or negative.
     * @throws ArithmeticException If a numeric overflow occurs.
     */
    @JvmStatic fun ofMiles(value: Double) = of(value, Mile)

    /**
     * Obtains a [Distance] representing a number of provided yards
     *
     * The meters and nanometers are extracted form the specified yards.
     *
     * @param value The amount of yards, positive or negative.
     * @throws ArithmeticException If a numeric overflow occurs.
     */
    @JvmStatic fun ofYards(value: Double) = of(value, Yard)

    /**
     * Obtains a [Distance] representing a number of provided feet
     *
     * The meters and nanometers are extracted form the specified feet.
     *
     * @param value The amount of feet, positive or negative.
     * @throws ArithmeticException If a numeric overflow occurs.
     */
    @JvmStatic fun ofFeet(value: Double) = of(value, Foot)

    /**
     * Obtains a [Distance] representing a number of provided inches
     *
     * The meters and nanometers are extracted form the specified inches.
     *
     * @param value The amount of inches, positive or negative.
     * @throws ArithmeticException If a numeric overflow occurs.
     */
    @JvmStatic fun ofInches(value: Double) = of(value, Inch)
  }
}
