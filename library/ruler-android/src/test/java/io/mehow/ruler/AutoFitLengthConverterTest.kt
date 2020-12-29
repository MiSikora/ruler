package io.mehow.ruler

import io.kotest.matchers.collections.shouldBeIn
import io.mehow.ruler.SiLengthUnit.Kilometer
import io.mehow.ruler.SiLengthUnit.Meter
import io.mehow.ruler.test.convert
import io.mehow.ruler.test.getApplicationContext
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class AutoFitLengthConverterTest {
  private val context = getApplicationContext()

  @Test fun `SI lengths are constrained to meters and kilometers`() {
    val lengths = List(100) { it.toLong() }.flatMap { value ->
      SiLengthUnit.units.map { Length.of(value, it) }
    }

    for (length in lengths) {
      AutoFitLengthConverter.convert(length, context).unit shouldBeIn listOf(Meter, Kilometer)
    }
  }

  @Test fun `imperial lengths are not constrained to any unit`() {
    val lengths = List(10) { it.toLong() }.flatMap { value ->
      ImperialLengthUnit.units.map { Length.of(value, it) }
    }

    for (length in lengths) {
      AutoFitLengthConverter.convert(length, context).unit shouldBeIn ImperialLengthUnit.units
    }
  }
}
