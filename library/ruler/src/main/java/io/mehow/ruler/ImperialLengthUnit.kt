package io.mehow.ruler

import java.math.BigDecimal

/**
 * Basic units from the imperial system.
 */
public enum class ImperialLengthUnit(
  override val meterRatio: BigDecimal,
  override val lowerBound: Distance,
  override val upperBound: Distance?,
) : LengthUnit<ImperialLengthUnit> {
  Inch(
      meterRatio = 0.025_4.toBigDecimal(),
      lowerBound = Distance.Zero,
      upperBound = Distance.create(nanometers = 304_800_000),
  ),
  Foot(
      meterRatio = 0.304_8.toBigDecimal(),
      lowerBound = Distance.create(nanometers = 304_800_000),
      upperBound = Distance.create(nanometers = 914_400_000),
  ),
  Yard(
      meterRatio = 0.914_4.toBigDecimal(),
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
