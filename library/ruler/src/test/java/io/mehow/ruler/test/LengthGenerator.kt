package io.mehow.ruler.test

import io.kotest.property.Arb
import io.kotest.property.RandomSource
import io.kotest.property.Sample
import io.mehow.ruler.Distance
import io.mehow.ruler.ImperialLengthUnit
import io.mehow.ruler.Length
import io.mehow.ruler.LengthUnit
import io.mehow.ruler.SiLengthUnit

internal class LengthGenerator<T : LengthUnit<T>> private constructor(
  ranges: List<ClosedRange<Distance>>,
  private vararg val units: T,
) : Arb<Length<T>>() {
  private val ranges = ranges.fold(emptyList<ClosedRange<Distance>>()) { ranges, range ->
    val useNewStart = ranges.any { range.endInclusive in it }
    val useNewEnd = ranges.any { range.start in it }
    when {
      !useNewStart && !useNewEnd -> ranges + range
      else -> ranges.map {
        val newStart = if (range.endInclusive in it) range.start else it.start
        val newEnd = if (range.start in it) range.endInclusive else it.endInclusive
        newStart..newEnd
      }
    }
  }

  private val distanceEdgeCases = this.ranges.flatMap { it.edgeCases() }.distinct()

  override fun edgecase(rs: RandomSource) = units.flatMap { unit ->
    distanceEdgeCases.map { distance -> distance.toLength(unit) }
  }.random(rs.random)

  override fun sample(rs: RandomSource) = rs.random
      .nextDistance(ranges.random())
      .toLength(units.random())
      .let(::Sample)

  companion object {
    fun si(
      min: Distance = Distance.Min,
      max: Distance = Distance.Max,
      vararg units: SiLengthUnit = SiLengthUnit.units.toTypedArray(),
    ) = LengthGenerator(listOf(min..max), *units)

    fun si(
      ranges: List<ClosedRange<Distance>>,
      vararg units: SiLengthUnit = SiLengthUnit.units.toTypedArray(),
    ) = LengthGenerator(ranges, *units)

    fun imperial(
      min: Distance = Distance.Min,
      max: Distance = Distance.Max,
      vararg units: ImperialLengthUnit = ImperialLengthUnit.units.toTypedArray(),
    ) = LengthGenerator(listOf(min..max), *units)

    fun imperial(
      ranges: List<ClosedRange<Distance>>,
      vararg units: ImperialLengthUnit = ImperialLengthUnit.units.toTypedArray(),
    ) = LengthGenerator(ranges, *units)

    fun create(
      min: Distance = Distance.Min,
      max: Distance = Distance.Max,
      siUnits: Collection<SiLengthUnit> = SiLengthUnit.units,
      imperialUnits: Collection<ImperialLengthUnit> = ImperialLengthUnit.units,
    ) = object : Arb<Length<*>>() {
      private val range = min..max
      private val units = siUnits + imperialUnits

      override fun edgecase(rs: RandomSource) = units.flatMap { unit ->
        range.edgeCases().map { distance -> distance.toLength(unit) }
      }.random(rs.random)

      override fun sample(rs: RandomSource) = rs.random
          .nextDistance(range)
          .toLength(units.random())
          .let(::Sample)
    }

    fun unitRange(unit: SiLengthUnit) = si(listOf(
        -unit.bounds.endInclusive..-unit.bounds.start,
        unit.bounds,
    ))

    fun unitRange(unit: ImperialLengthUnit) = imperial(listOf(
        -unit.bounds.endInclusive..-unit.bounds.start,
        unit.bounds,
    ))

    fun unitRange(unit: LengthUnit<*>) = when (unit) {
      is SiLengthUnit -> unitRange(unit)
      is ImperialLengthUnit -> unitRange(unit)
    }

    private fun ClosedRange<Distance>.edgeCases() = listOfNotNull(
        start,
        start + Distance.Epsilon,
        Distance.Zero,
        endInclusive - Distance.Epsilon,
        endInclusive,
    ).filter { it in this }
  }
}
