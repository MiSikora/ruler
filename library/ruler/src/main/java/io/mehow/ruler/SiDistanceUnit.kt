package io.mehow.ruler

import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode.CEILING
import kotlin.Double.Companion.MAX_VALUE

enum class SiDistanceUnit(
  private val applicableRange: ClosedRange<BigDecimal>,
  private val meterRatio: BigDecimal
) : DistanceUnit, Iterable<SiDistanceUnit> {
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
    override fun appliesRangeTo(meters: BigDecimal) = meters >= super.applicableRange.start
  };

  override fun toLength(value: BigInteger) = when {
    this == Meter -> Length.create(meters = value)
    meterRatio > 1.0.toBigDecimal() -> aboveMeterToLength(value)
    else -> belowMeterToLength(value)
  }

  private fun aboveMeterToLength(value: BigInteger): Length {
    val meters = value * meterRatio.toLong().toBigInteger()
    return Length.create(meters = meters)
  }

  private fun belowMeterToLength(value: BigInteger): Length {
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
