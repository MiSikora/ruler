package io.mehow.ruler

import java.math.BigDecimal
import kotlin.Double.Companion.MAX_VALUE

enum class ImperialLengthUnit(
  private val applicableRange: ClosedRange<BigDecimal>,
  override val meterRatio: BigDecimal,
) : LengthUnit<ImperialLengthUnit> {
  Inch(
      0.0.toBigDecimal()..0.3_048.toBigDecimal(),
      0.0_254.toBigDecimal(),
  ),
  Foot(
      0.3_048.toBigDecimal()..0.9_144.toBigDecimal(),
      0.3_048.toBigDecimal(),
  ),
  Yard(
      0.9_144.toBigDecimal()..1_609.3.toBigDecimal(),
      0.9_144.toBigDecimal(),
  ),
  Mile(
      1_609.3.toBigDecimal()..MAX_VALUE.toBigDecimal(),
      1_609.344.toBigDecimal(),
  ) {
    override fun appliesRangeTo(meters: BigDecimal): Boolean {
      return meters >= super.applicableRange.start
    }
  };

  override fun appliesRangeTo(meters: BigDecimal): Boolean {
    return meters >= applicableRange.start && meters < applicableRange.endInclusive
  }

  override fun iterator() = values.iterator()

  companion object {
    private val values = values().toList()
  }
}
