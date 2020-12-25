package io.mehow.ruler

import java.math.BigDecimal

public enum class SiLengthUnit(
  override val meterRatio: BigDecimal,
  private val lowerBound: BigDecimal,
  private val upperBound: BigDecimal?,
) : LengthUnit<SiLengthUnit> {
  Nanometer(
      meterRatio = 0.000_000_001.toBigDecimal(),
      lowerBound = 0.0.toBigDecimal(),
      upperBound = 0.000_001.toBigDecimal(),
  ),
  Micrometer(
      meterRatio = 0.000_001.toBigDecimal(),
      lowerBound = 0.000_001.toBigDecimal(),
      upperBound = 0.001.toBigDecimal(),
  ),
  Millimeter(
      meterRatio = 0.001.toBigDecimal(),
      lowerBound = 0.001.toBigDecimal(),
      upperBound = 1.0.toBigDecimal(),
  ),
  Meter(
      meterRatio = 1.0.toBigDecimal(),
      lowerBound = 1.0.toBigDecimal(),
      upperBound = 1_000.0.toBigDecimal(),
  ),
  Kilometer(
      meterRatio = 1_000.0.toBigDecimal(),
      lowerBound = 1_000.0.toBigDecimal(),
      upperBound = 1_000_000.0.toBigDecimal(),
  ),
  Megameter(
      meterRatio = 1_000_000.0.toBigDecimal(),
      lowerBound = 1_000_000.0.toBigDecimal(),
      upperBound = 1_000_000_000.0.toBigDecimal(),
  ),
  Gigameter(
      meterRatio = 1_000_000_000.0.toBigDecimal(),
      lowerBound = 1_000_000_000.0.toBigDecimal(),
      upperBound = null,
  );

  override operator fun contains(meters: BigDecimal): Boolean {
    val inLowerBound = meters >= lowerBound
    val inUpperBound = upperBound == null || meters < upperBound
    return inLowerBound && inUpperBound
  }

  override fun iterator(): Iterator<SiLengthUnit> = values.iterator()

  public companion object {
    private val values = values().toList()
  }
}
