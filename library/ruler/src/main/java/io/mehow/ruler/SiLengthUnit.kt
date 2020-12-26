package io.mehow.ruler

import java.math.BigDecimal

/**
 * Meter based units.
 */
public enum class SiLengthUnit(
  override val meterRatio: BigDecimal,
  override val lowerBound: Distance,
  override val upperBound: Distance?,
) : LengthUnit<SiLengthUnit> {
  Nanometer(
      meterRatio = 0.000_000_001.toBigDecimal(),
      lowerBound = Distance.Zero,
      upperBound = Distance.create(nanometers = 1_000),
  ),
  Micrometer(
      meterRatio = 0.000_001.toBigDecimal(),
      lowerBound = Distance.create(nanometers = 1_000),
      upperBound = Distance.create(nanometers = 1_000_000),
  ),
  Millimeter(
      meterRatio = 0.001.toBigDecimal(),
      lowerBound = Distance.create(nanometers = 1_000_000),
      upperBound = Distance.create(meters = 1),
  ),
  Meter(
      meterRatio = BigDecimal.ONE,
      lowerBound = Distance.create(meters = 1),
      upperBound = Distance.create(meters = 1_000),
  ),
  Kilometer(
      meterRatio = 1_000.0.toBigDecimal(),
      lowerBound = Distance.create(meters = 1_000),
      upperBound = Distance.create(meters = 1_000_000),
  ),
  Megameter(
      meterRatio = 1_000_000.0.toBigDecimal(),
      lowerBound = Distance.create(meters = 1_000_000),
      upperBound = Distance.create(meters = 1_000_000_000),
  ),
  Gigameter(
      meterRatio = 1_000_000_000.0.toBigDecimal(),
      lowerBound = Distance.create(meters = 1_000_000_000),
      upperBound = null,
  ),
  ;
}
