package io.mehow.ruler.format

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.mehow.ruler.Distance
import io.mehow.ruler.Length
import io.mehow.ruler.Ruler
import io.mehow.ruler.SiLengthUnit.Nanometer
import io.mehow.ruler.test.ResetRulerListener
import io.mehow.ruler.test.localize
import java.util.Locale

internal class AutoFormatterSpec : DescribeSpec({
  listener(ResetRulerListener)

  describe("formatter") {
    it("displays max length without overflow") {
      val length = Distance.Max.toLength(Nanometer)

      length.format(converter = null, formatter = NoOpFormatter) shouldBe "9,223,372,036,854,775,807,999,999,999.00nm"
    }

    it("displays min length without overflow") {
      val length = Distance.Min.toLength(Nanometer)

      length.format(converter = null, formatter = NoOpFormatter) shouldBe "-9,223,372,036,854,775,807,999,999,999.00nm"
    }

    it("displays a custom separator in a correct position") {
      val length = Length.ofMeters(12.36)

      val context = FormattingContext.withSeparator("|")
      length.format(context, converter = null, formatter = NoOpFormatter) shouldBe "12.36|m"
    }

    it("uses custom fractional precision") {
      val length = Length.ofMeters(15.73169)

      val context = FormattingContext.withPrecision(4)
      length.format(context, converter = null, formatter = NoOpFormatter) shouldBe "15.7317m"
    }

    it("applies locale for digits grouping") {
      val length = Length.ofMeters(1200)

      length.format(converter = null, formatter = NoOpFormatter) shouldBe "1,200.00m"

      Ruler.localize(Locale("pl"))
      length.format(converter = null, formatter = NoOpFormatter) shouldBe "1 200,00m"
    }

    it("displays all units properly") {
      forAll(
          row(Length.ofGigameters(15.642), "15.64Gm"),
          row(Length.ofMegameters(19.091), "19.09Mm"),
          row(Length.ofKilometers(891.119), "891.12km"),
          row(Length.ofHectometers(111.222), "111.22hm"),
          row(Length.ofDecameters(6_314.321), "6,314.32dam"),
          row(Length.ofMeters(7.111), "7.11m"),
          row(Length.ofDecimeters(895.0), "895.00dm"),
          row(Length.ofCentimeters(11_312.820_123), "11,312.82cm"),
          row(Length.ofMicrometers(904.55), "904.55µm"),
          row(Length.ofNanometers(12.09), "12.00nm"),
          row(Length.ofMiles(338.1), "338.10mi"),
          row(Length.ofYards(120.120), "120.12yd"),
          row(Length.ofFeet(17.999), "18.00ft"),
          row(Length.ofInches(999.99999), "1,000.00in"),
      ) { length, expectation ->
        length.format(converter = null, formatter = NoOpFormatter) shouldBe expectation
      }
    }
  }
})
