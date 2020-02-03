package io.mehow.ruler

import io.kotlintest.properties.Gen
import kotlin.Double.Companion.MAX_VALUE
import kotlin.Double.Companion.MIN_VALUE
import kotlin.random.Random

class DoubleGenerator(
  private val min: Double = MIN_VALUE,
  private val max: Double = MAX_VALUE
) : Gen<Double> {
  override fun constants() = listOf(min, min / 2 + max / 2, max)

  override fun random() = generateSequence { Random.nextDouble(min, max) }
}
