package io.mehow.ruler

import java.math.BigDecimal

interface DistanceUnit {
  fun appliesRangeTo(length: BigDecimal): Boolean

  fun toLength(value: Long): Length

  fun toMeasuredLength(length: BigDecimal): Double
}
