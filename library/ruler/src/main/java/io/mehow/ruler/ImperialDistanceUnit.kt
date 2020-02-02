package io.mehow.ruler

import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode.CEILING
import kotlin.Double.Companion.MAX_VALUE

enum class ImperialDistanceUnit(
  private val applicableRange: ClosedRange<BigDecimal>,
  private val meterRatio: BigDecimal
) : DistanceUnit, Iterable<ImperialDistanceUnit> {
  Inch(BigDecimal.ZERO..0.3048.toBigDecimal(), 0.0254.toBigDecimal()),
  Foot(0.3048.toBigDecimal()..0.9144.toBigDecimal(), 0.3048.toBigDecimal()),
  Yard(0.9144.toBigDecimal()..1_609.3.toBigDecimal(), 0.9144.toBigDecimal()),
  Mile(1_609.3.toBigDecimal()..MAX_VALUE.toBigDecimal(), 1_609.344.toBigDecimal()) {
    override fun appliesRangeTo(meters: BigDecimal) = meters >= super.applicableRange.start
  };

  override fun toLength(value: BigInteger): Length {
    val meters = value.toBigDecimal() * meterRatio
    val exactMeters = meters.toBigInteger()
    val nanometers = ((meters - exactMeters.toBigDecimal()) * 1_000_000_000.toBigDecimal()).toLong()
    return Length.create(meters = exactMeters, nanometers = nanometers)
  }

  override fun toMeasuredLength(meters: BigDecimal) = meters.divide(meterRatio, 9, CEILING)

  override fun iterator() = values.iterator()

  override fun appliesRangeTo(meters: BigDecimal): Boolean {
    return meters >= applicableRange.start && meters < applicableRange.endInclusive
  }

  companion object {
    private val values = values().toList()
  }
}
