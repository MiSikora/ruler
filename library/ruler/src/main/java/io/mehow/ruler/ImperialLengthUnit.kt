package io.mehow.ruler

import java.math.BigDecimal
import java.math.RoundingMode.DOWN
import kotlin.Double.Companion.MAX_VALUE

enum class ImperialLengthUnit(
  private val applicableRange: ClosedRange<BigDecimal>,
  private val meterRatio: BigDecimal
) : LengthUnit, Iterable<ImperialLengthUnit> {
  Inch(0.0.toBigDecimal()..0.3048.toBigDecimal(), 0.0254.toBigDecimal()),
  Foot(0.3048.toBigDecimal()..0.9144.toBigDecimal(), 0.3048.toBigDecimal()),
  Yard(0.9144.toBigDecimal()..1_609.3.toBigDecimal(), 0.9144.toBigDecimal()),
  Mile(1_609.3.toBigDecimal()..MAX_VALUE.toBigDecimal(), 1_609.344.toBigDecimal()) {
    override fun appliesRangeTo(distance: BigDecimal): Boolean {
      return distance >= super.applicableRange.start
    }
  };

  override fun toDistance(value: Long): Distance {
    return Distance.create(value.toBigDecimal() * meterRatio)
  }

  override fun toDistance(value: Double): Distance {
    return Distance.create(value.toBigDecimal() * meterRatio)
  }

  override fun toMeasuredLength(distance: BigDecimal): Double {
    return distance.divide(meterRatio, 9, DOWN).toDouble()
  }

  override fun appliesRangeTo(distance: BigDecimal): Boolean {
    return distance >= applicableRange.start && distance < applicableRange.endInclusive
  }

  override fun iterator() = values.iterator()

  companion object {
    private val values = values().toList()
  }
}
