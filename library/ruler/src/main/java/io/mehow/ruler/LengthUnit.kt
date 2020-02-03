package io.mehow.ruler

import java.math.BigDecimal

interface LengthUnit {
  fun toDistance(value: Long): Distance

  fun toDistance(value: Double): Distance

  fun toMeasuredLength(distance: BigDecimal): Double

  fun appliesRangeTo(distance: BigDecimal): Boolean
}
