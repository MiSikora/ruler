package io.mehow.ruler

import io.kotlintest.properties.Gen
import kotlin.Long.Companion.MAX_VALUE
import kotlin.Long.Companion.MIN_VALUE
import kotlin.random.Random

class LongGenerator(
  private val min: Long = MIN_VALUE,
  private val max: Long = MAX_VALUE
) : Gen<Long> {
  override fun constants() = listOf(min, min / 2 + max / 2, max)

  override fun random() = generateSequence { Random.nextLong(min, max) }
}
