package io.mehow.ruler

import java.math.BigDecimal
import java.math.RoundingMode.CEILING
import kotlin.Double.Companion.MAX_VALUE

enum class ImperialDistanceUnit(
  private val applicableRange: ClosedRange<BigDecimal>,
  private val meterRatio: BigDecimal
) : DistanceUnit, Iterable<ImperialDistanceUnit> {
  Inch(0.0.toBigDecimal()..0.3048.toBigDecimal(), 0.0254.toBigDecimal()),
  Foot(0.3048.toBigDecimal()..0.9144.toBigDecimal(), 0.3048.toBigDecimal()),
  Yard(0.9144.toBigDecimal()..1_609.3.toBigDecimal(), 0.9144.toBigDecimal()),
  Mile(1_609.3.toBigDecimal()..MAX_VALUE.toBigDecimal(), 1_609.344.toBigDecimal()) {
    override fun appliesRangeTo(length: BigDecimal): Boolean {
      return length >= super.applicableRange.start
    }
  };

  override fun toLength(value: Long): Length {
    val meters = value.toBigDecimal() * meterRatio
    val exactMeters = meters.toBigInteger().longValueExact()
    val nanometers = (meters - exactMeters.toBigDecimal()) * 1_000_000_000.toBigDecimal()
    return Length.create(exactMeters, nanometers.toLong())
  }

  override fun toMeasuredLength(length: BigDecimal): Double {
    return length.divide(meterRatio, 9, CEILING).toDouble()
  }

  override fun iterator() = values.iterator()

  override fun appliesRangeTo(length: BigDecimal): Boolean {
    return length >= applicableRange.start && length < applicableRange.endInclusive
  }

  companion object {
    private val values = values().toList()
  }
}
