package io.mehow.ruler

import io.kotlintest.properties.Gen
import kotlin.random.Random

object DistanceUnitGenerator : Gen<LengthUnit> {
  override fun constants(): Iterable<LengthUnit> {
    val siLengthUnits: Iterable<LengthUnit> = SiLengthUnit.values().toList()
    val imperialLengthUnits: Iterable<LengthUnit> = ImperialLengthUnit.values().toList()
    return siLengthUnits + imperialLengthUnits
  }

  override fun random() = generateSequence {
    val useSi = Random.nextBoolean()
    @Suppress("UnnecessaryVariable")
    val lengthUnit: LengthUnit = if (useSi) SiLengthUnit.values().random()
    else ImperialLengthUnit.values().random()
    return@generateSequence lengthUnit
  }

  fun createLength(distance: Distance, unit: LengthUnit) = when (unit) {
    is SiLengthUnit -> Length(distance, unit)
    is ImperialLengthUnit -> Length(distance, unit)
    else -> error("Unknown unit: $unit")
  }
}
