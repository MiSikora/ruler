package io.mehow.ruler.test

import io.mehow.ruler.Distance
import io.mehow.ruler.SiLengthUnit.Meter
import kotlin.random.Random

internal fun Random.nextDistance(range: ClosedRange<Distance>): Distance {
  val min = range.start.meters.toDouble()
  val max = range.endInclusive.meters.toDouble()
  return Distance.of(nextDouble(min, max), Meter)
}
