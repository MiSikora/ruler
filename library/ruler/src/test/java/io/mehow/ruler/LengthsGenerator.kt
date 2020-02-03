package io.mehow.ruler

import io.kotlintest.properties.Gen
import kotlin.random.Random

class LengthsGenerator(
  private val min: Length = Length.Zero,
  private val max: Length = Length.Max
) : Gen<Length> {
  override fun constants() = listOf(min, max)

  override fun random() = generateSequence { createLength() }

  private fun createLength(): Length {
    val meters = if (min.metersPart == max.metersPart) min.metersPart
    else Random.nextLong(min.metersPart, max.metersPart)

    val nanometers = when {
      meters > min.metersPart -> Random.nextLong(1_000_000_000)
      meters < max.metersPart -> Random.nextLong(1_000_000_000)
      meters == max.metersPart && meters == min.metersPart -> Random.nextLong(min.nanosPart, max.nanosPart)
      meters == min.metersPart -> Random.nextLong(min.nanosPart, 1_000_000_000)
      meters == max.metersPart -> Random.nextLong(0, max.nanosPart)
      else -> error("Unexpected range: min=${min.metersPart}, max=${max.metersPart}, value=${meters}")
    }
    val candidate = Length.create(meters, nanometers)
    return if (candidate in min..max) candidate else createLength()
  }
}
