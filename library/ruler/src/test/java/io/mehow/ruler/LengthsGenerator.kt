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
      min.metersPart == max.metersPart -> min.metersPart
      else -> Random.nextLong(min.metersPart.toLong(), max.metersPart.toLong()).toBigInteger()
    }
    val nanometers = when {
      meters > min.metersPart -> Random.nextLong(1_000_000_000)
      meters < max.metersPart -> Random.nextLong(1_000_000_000)
      meters == max.metersPart && meters == min.metersPart -> Random.nextLong(min.nanometersPart, max.nanometersPart)
      meters == min.metersPart -> Random.nextLong(min.nanometersPart, 1_000_000_000)
      meters == max.metersPart -> Random.nextLong(0, max.nanometersPart)
      else -> error("Unexpected condition: min=${min.metersPart}, max=${max.metersPart}, value=${meters}")
    }
    val candidate = Length.create(meters, nanometers)
    return if (candidate in min..max) candidate else createLength()
  }
}
