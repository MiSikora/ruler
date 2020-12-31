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

  private val distanceEdgeCases = this.ranges.flatMap { range ->
    listOfNotNull(
        range.start,
        range.start + Distance.Epsilon,
        Distance.Zero,
        range.endInclusive - Distance.Epsilon,
        range.endInclusive,
    ).filter { it in range }
  }.distinct()

  override fun edgecases() = units.flatMap { unit ->
    distanceEdgeCases.map { distance -> distance.toLength(unit) }
  }

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
      vararg units: LengthUnit<*> = (SiLengthUnit.units + ImperialLengthUnit.units).toTypedArray(),
    ) = LengthGenerator<LengthUnit<*>>(listOf(min..max), *units)

    fun create(
      ranges: List<ClosedRange<Distance>>,
      vararg units: LengthUnit<*> = (SiLengthUnit.units + ImperialLengthUnit.units).toTypedArray(),
    ) = LengthGenerator<LengthUnit<*>>(ranges, *units)

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
  }
}