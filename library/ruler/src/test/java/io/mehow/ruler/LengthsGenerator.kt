package io.mehow.ruler

import io.kotlintest.properties.Gen
import kotlin.Long.Companion.MAX_VALUE
import kotlin.random.Random

class LengthsGenerator(
  private val min: Length = Length.Zero,
  private val max: Length = Length.create(MAX_VALUE, 999_999_999)
) : Gen<Length> {
  override fun constants() = listOf(min, max)

  override fun random() = generateSequence { createLength() }

  private fun createLength(): Length {
    val meters = when {
      min.meters == max.meters -> min.meters
      else -> Random.nextLong(min.meters.toLong(), max.meters.toLong()).toBigInteger()
    }
    val nanometers = when {
      meters > min.meters -> Random.nextLong(1_000_000_000)
      meters < max.meters -> Random.nextLong(1_000_000_000)
      meters == max.meters && meters == min.meters -> Random.nextLong(min.nanometers, max.nanometers)
      meters == min.meters -> Random.nextLong(min.nanometers, 1_000_000_000)
      meters == max.meters -> Random.nextLong(0, max.nanometers)
      else -> error("Unexpected condition: min=${min.meters}, max=${max.meters}, value=${meters}")
    }
    val candidate = Length.create(meters, nanometers)
    return if (candidate in min..max) candidate else createLength()
  }
}
