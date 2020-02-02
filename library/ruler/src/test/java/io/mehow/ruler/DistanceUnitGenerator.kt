package io.mehow.ruler

import io.kotlintest.properties.Gen
import kotlin.random.Random

object DistanceUnitGenerator : Gen<DistanceUnit> {
  override fun constants(): Iterable<DistanceUnit> {
    val siDistanceUnits: Iterable<DistanceUnit> = SiDistanceUnit.values().toList()
    val imperialDistanceUnits: Iterable<DistanceUnit> = ImperialDistanceUnit.values().toList()
    return siDistanceUnits + imperialDistanceUnits
  }

  override fun random() = generateSequence {
    val useSi = Random.nextBoolean()
    @Suppress("UnnecessaryVariable")
    val distanceUnit: DistanceUnit = if (useSi) SiDistanceUnit.values().random()
    else ImperialDistanceUnit.values().random()
    return@generateSequence distanceUnit
  }

  fun createDistance(length: Length, unit: DistanceUnit) = when (unit) {
    is SiDistanceUnit -> Distance(length, unit)
    is ImperialDistanceUnit -> Distance(length, unit)
    else -> error("Unknown unit: $unit")
  }
}
