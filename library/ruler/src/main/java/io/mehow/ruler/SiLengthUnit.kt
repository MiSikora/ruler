package io.mehow.ruler

import io.mehow.ruler.Distance.Companion.create
import java.math.BigDecimal
import java.math.RoundingMode.DOWN
import kotlin.Double.Companion.MAX_VALUE

/**
 * Length units based on the SI measurement system.
 */
enum class SiLengthUnit(
  private val applicableRange: ClosedRange<BigDecimal>,
  private val meterRatio: BigDecimal
) : LengthUnit<SiLengthUnit> {
  /**
   * Represents 0.000,000,001 meters.
   */
  Nanometer(
      0.0.toBigDecimal()..0.000_001.toBigDecimal(),
      0.000_000_001.toBigDecimal()
  ),
  /**
   * Represents 0.000,001 meters.
   */
  Micrometer(
      0.000_001.toBigDecimal()..0.001.toBigDecimal(),
      0.000_001.toBigDecimal()
  ),
  /**
   * Represents 0.001 meters.
   */
  Millimeter(
      0.001.toBigDecimal()..1.0.toBigDecimal(),
      0.001.toBigDecimal()
  ),
  /**
   * Represents 1 meter.
   */
  Meter(
      1.0.toBigDecimal()..1_000.0.toBigDecimal(),
      1.0.toBigDecimal()
  ),
  /**
   * Represents 1000 meter.
   */
  Kilometer(
      1_000.0.toBigDecimal()..1_000_000.0.toBigDecimal(),
      1_000.0.toBigDecimal()
  ),
  /**
   * Represents 1000000 meter.
   */
  Megameter(
      1_000_000.0.toBigDecimal()..1_000_000_000.0.toBigDecimal(),
      1_000_000.0.toBigDecimal()
  ),
  /**
   * Represents 1000000000 meter.
   */
  Gigameter(
      1_000_000_000.0.toBigDecimal()..MAX_VALUE.toBigDecimal(),
      1_000_000_000.0.toBigDecimal()
  ) {
    override fun appliesRangeTo(meters: BigDecimal): Boolean {
      return meters >= super.applicableRange.start
    }
  };

  override fun toDistance(value: Double): Distance {
    return create(value.toBigDecimal() * meterRatio)
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
