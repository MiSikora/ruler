package io.mehow.ruler

import io.kotest.property.Arb
import io.kotest.property.RandomSource
import io.kotest.property.Sample
import kotlin.random.Random

class DistanceGenerator(
  private val min: Distance = Distance.Min,
  private val max: Distance = Distance.Max,
) : Arb<Distance>() {
  override fun sample(rs: RandomSource) = Sample(createDistance(rs.random))

  override fun edgecases() = listOf(min, max)

  private fun createDistance(random: Random): Distance {
    val meters = if (min.metersPart == max.metersPart) min.metersPart
    else random.nextLong(min.metersPart, max.metersPart)

    val nanometers = when {
      meters > min.metersPart -> random.nextLong(1_000_000_000)
      meters < max.metersPart -> random.nextLong(1_000_000_000)
      meters == max.metersPart && meters == min.metersPart -> random.nextLong(min.nanosPart, max.nanosPart)
      meters == min.metersPart -> random.nextLong(min.nanosPart, 1_000_000_000)
      meters == max.metersPart -> random.nextLong(0, max.nanosPart)
      else -> error("Unexpected range: min=${min.metersPart}, max=${max.metersPart}, value=${meters}")
    }
    val candidate = Distance.create(meters, nanometers)
    return if (candidate in min..max) candidate else createDistance(random)
  }
}
