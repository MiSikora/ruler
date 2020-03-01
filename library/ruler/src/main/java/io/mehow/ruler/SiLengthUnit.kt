package io.mehow.ruler

import io.mehow.ruler.Distance.Companion.create
import java.math.BigDecimal
import java.math.RoundingMode.DOWN
import kotlin.Double.Companion.MAX_VALUE

enum class SiLengthUnit(
  private val applicableRange: ClosedRange<BigDecimal>,
  private val meterRatio: BigDecimal
) : LengthUnit<SiLengthUnit> {
  Nanometer(
      0.0.toBigDecimal()..0.000_001.toBigDecimal(),
      0.000_000_001.toBigDecimal()
  ),
  Micrometer(
      0.000_001.toBigDecimal()..0.001.toBigDecimal(),
      0.000_001.toBigDecimal()
  ),
  Millimeter(
      0.001.toBigDecimal()..1.0.toBigDecimal(),
      0.001.toBigDecimal()
  ),
  Meter(
      1.0.toBigDecimal()..1_000.0.toBigDecimal(),
      1.0.toBigDecimal()
  ),
  Kilometer(
      1_000.0.toBigDecimal()..1_000_000.0.toBigDecimal(),
      1_000.0.toBigDecimal()
  ),
  Megameter(
      1_000_000.0.toBigDecimal()..1_000_000_000.0.toBigDecimal(),
      1_000_000.0.toBigDecimal()
  ),
  Gigameter(
      1_000_000_000.0.toBigDecimal()..MAX_VALUE.toBigDecimal(),
      1_000_000_000.0.toBigDecimal()
  ) {
    override fun appliesRangeTo(meters: BigDecimal): Boolean {
      return meters >= super.applicableRange.start
    }
  };

  override fun toDistance(value: Long): Distance {
    return create(value.toBigDecimal() * meterRatio)
  }

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
