package io.mehow.ruler

import java.math.BigDecimal

public enum class ImperialLengthUnit(
  override val meterRatio: BigDecimal,
  override val lowerBound: Distance,
  override val upperBound: Distance?,
) : LengthUnit<ImperialLengthUnit> {
  Inch(
      meterRatio = 0.0_254.toBigDecimal(),
      lowerBound = Distance.Zero,
      upperBound = Distance.create(nanometers = 304_800_000),
  ),
  Foot(
      meterRatio = 0.3_048.toBigDecimal(),
      lowerBound = Distance.create(nanometers = 304_800_000),
      upperBound = Distance.create(nanometers = 914_400_000),
  ),
  Yard(
      meterRatio = 0.9_144.toBigDecimal(),
      lowerBound = Distance.create(nanometers = 914_400_000),
      upperBound = Distance.create(meters = 1_609, nanometers = 344_000_000),
  ),
  Mile(
      meterRatio = 1_609.344.toBigDecimal(),
      lowerBound = Distance.create(meters = 1_609, nanometers = 344_000_000),
      upperBound = null,
  ),
  ;
}
