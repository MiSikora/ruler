package io.mehow.ruler

import java.math.BigDecimal

interface LengthUnit {
  fun appliesRangeTo(distance: BigDecimal): Boolean

  fun toDistance(value: Long): Distance

  fun toMeasuredLength(distance: BigDecimal): Double
}
