package io.mehow.ruler.test

import io.kotest.property.Arb
import io.kotest.property.RandomSource
import io.kotest.property.Sample
import io.mehow.ruler.Distance

internal class DistanceGenerator private constructor(
  private val range: ClosedRange<Distance>,
) : Arb<Distance>() {
  override fun edgecases() = (setOf(range.start, range.endInclusive) + when (Distance.Zero) {
    in range -> setOf(Distance.Zero)
    else -> emptySet()
  }).toList()

  override fun sample(rs: RandomSource) = rs.random
      .nextDistance(range)
      .let(::Sample)

  companion object {
    fun create(
      min: Distance = Distance.Min,
      max: Distance = Distance.Max,
    ) = DistanceGenerator(min..max)
  }
}
