package io.mehow.ruler

import java.math.BigDecimal
import java.math.RoundingMode.DOWN
import kotlin.Double.Companion.MAX_VALUE

/**
 * Length units based on the imperial measurement system.
 */
enum class ImperialLengthUnit(
  private val applicableRange: ClosedRange<BigDecimal>,
  private val meterRatio: BigDecimal
) : LengthUnit<ImperialLengthUnit> {
  /**
   * Represents 0.0254 meters.
   */
  Inch(0.0.toBigDecimal()..0.3_048.toBigDecimal(), 0.0_254.toBigDecimal()),
  /**
   * Represents 0.3048 meters.
   */
  Foot(0.3_048.toBigDecimal()..0.9_144.toBigDecimal(), 0.3_048.toBigDecimal()),
  /**
   * Represents 0.9144 meters.
   */
  Yard(0.9_144.toBigDecimal()..1_609.3.toBigDecimal(), 0.9_144.toBigDecimal()),
  /**
   * Represents 1609.344 meters.
   */
  Mile(1_609.3.toBigDecimal()..MAX_VALUE.toBigDecimal(), 1_609.344.toBigDecimal()) {
    override fun appliesRangeTo(meters: BigDecimal): Boolean {
      return meters >= super.applicableRange.start
    }
  };

  override fun toDistance(value: Double): Distance {
    return Distance.create(value.toBigDecimal() * meterRatio)
  }

  override fun toMeasuredLength(meters: BigDecimal): BigDecimal {
    return meters.divide(meterRatio, 9, DOWN)
  }

  override fun appliesRangeTo(meters: BigDecimal): Boolean {
    return meters >= applicableRange.start && meters < applicableRange.endInclusive
  }

  override fun iterator() = values.iterator()

  companion object {
    private val values = values().toList()
  }
}
