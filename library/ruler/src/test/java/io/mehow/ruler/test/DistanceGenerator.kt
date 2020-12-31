package io.mehow.ruler.test

import io.kotest.property.Arb
import io.kotest.property.RandomSource
import io.kotest.property.Sample
import io.mehow.ruler.Distance
import io.mehow.ruler.LengthUnit

internal class DistanceGenerator private constructor(
  private val range: ClosedRange<Distance>,
) : Arb<Distance>() {
  private val edgeCases = listOfNotNull(
      range.start,
      range.start + Distance.Epsilon,
      Distance.Zero,
      range.endInclusive - Distance.Epsilon,
      range.endInclusive,
  ).filter { it in range }.distinct()

  override fun edgecases() = edgeCases

  override fun sample(rs: RandomSource) = rs.random
      .nextDistance(range)
      .let(::Sample)

  companion object {
    fun create(
      min: Distance = Distance.Min,
      max: Distance = Distance.Max,
    ) = DistanceGenerator(min..max)

    fun create(unit: LengthUnit<*>) = DistanceGenerator(unit.bounds)
  }
}
