package io.mehow.ruler

import java.math.BigDecimal
import java.math.BigInteger

interface DistanceUnit {
  fun appliesRangeTo(meters: BigDecimal): Boolean

  fun toLength(value: BigInteger): Length

  @JvmDefault fun toLength(value: Long): Length = toLength(value.toBigInteger())

  fun toMeasuredLength(meters: BigDecimal): BigDecimal
}
