package io.mehow.ruler

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import io.mehow.ruler.SiLengthUnit.Meter
import io.mehow.ruler.format.FormattingContext
import io.mehow.ruler.format.LengthConverter
import io.mehow.ruler.format.LengthFormatter
import io.mehow.ruler.test.ResetRulerListener
import io.mehow.ruler.test.localize
import java.util.Locale

internal class RulerSpec : DescribeSpec({
  listener(ResetRulerListener)

  describe("ruler") {
    context("configuration") {
      it("has built-in converter factories") {
        Ruler.installedConverterFactories.shouldNotBeEmpty()
      }

      it("cannot remove built-in converter factories") {
        val factories = Ruler.installedConverterFactories

        factories.forEach(Ruler::removeConverterFactory)

        Ruler.installedConverterFactories shouldContainExactly factories
      }

      it("can add a converter factory") {
        val factory = LengthConverter.Factory { null }

        Ruler.addConverterFactory(factory)

        Ruler.installedConverterFactories shouldContain factory
      }

      it("can remove a converter factory") {
        val factory = LengthConverter.Factory { null }

        Ruler.addConverterFactory(factory)
        Ruler.removeConverterFactory(factory)

        Ruler.installedConverterFactories shouldNotContain factory
      }

      it("uses converter factories in the installed order") {
        var text = ""
        Ruler.addConverterFactory { text += "a"; null }
        Ruler.addConverterFactory { text += "b"; null }

        Distance.Zero.format()

        text shouldBe "ab"
      }

      it("does not use following converters after first match") {
        var text = ""
        Ruler.addConverterFactory { text += "a"; LengthConverter { it } }
        Ruler.addConverterFactory { text += "b"; null }

        Distance.Zero.format()

        text shouldBe "a"
      }

      it("has built-in formatter factories") {
        Ruler.installedFormatterFactories.shouldNotBeEmpty()
      }

      it("cannot remove built-in formatter factories") {
        val factories = Ruler.installedFormatterFactories

        factories.forEach(Ruler::removeFormatterFactory)

        Ruler.installedFormatterFactories shouldContainExactly factories
      }

      it("can add a formatter factory") {
        val factory = LengthFormatter.Factory { _, _ -> null }

        Ruler.addFormatterFactory(factory)

        Ruler.installedFormatterFactories shouldContain factory
      }

      it("can remove a formatter factory") {
        val factory = LengthFormatter.Factory { _, _ -> null }

        Ruler.addFormatterFactory(factory)
        Ruler.removeFormatterFactory(factory)

        Ruler.installedFormatterFactories shouldNotContain factory
      }

      it("uses formatter factories in the installed order") {
        var text = ""
        Ruler.addFormatterFactory { _, _ -> text += "a"; null }
        Ruler.addFormatterFactory { _, _ -> text += "b"; null }

        Distance.Zero.format()

        text shouldBe "ab"
      }

      it("does not use following formatters after first match") {
        var text = ""
        Ruler.addFormatterFactory { _, _ -> text += "a"; LengthFormatter { _, _ -> "" } }
        Ruler.addFormatterFactory { _, _ -> text += "b"; null }

        Distance.Zero.format()

        text shouldBe "a"
      }

      it("does not use imperial formatting for UK by default") {
        Ruler.localize(Locale.UK)

        Distance.ofFeet(4).format() shouldBe "1.22m"
      }

      it("can turn on imperial formatting for UK") {
        Ruler.localize(Locale.UK)

        Ruler.isUkImperial = true

        Distance.ofFeet(4).format() shouldBe "1yd 1ft"
      }

      it("can turn off imperial formatting for UK") {
        Ruler.localize(Locale.UK)

        Ruler.isUkImperial = true
        Ruler.isUkImperial = false

        Distance.ofFeet(4).format() shouldBe "1.22m"
      }
    }

    context("formatting") {
      it("uses imperial formatting for imperial lengths") {
        Length.ofFeet(1.5).format() shouldBe "1ft 6in"
      }

      it("does not use imperial formatting for SI lengths") {
        Length.ofMeters(1).format() shouldBe "1.00m"
      }

      it("can turn off imperial formatting") {
        Ruler.useImperialFormatter = false

        Length.ofFeet(1.5).format() shouldBe "1.50ft"
      }

      it("uses all units for imperial formatting") {
        val length = Length.ofInches(1) +
            Length.ofFeet(2) +
            Length.ofYards(3) +
            Length.ofMiles(4)

        length.format() shouldBe "4mi 3yd 2ft 1in"
      }

      it("uses meters as default SI units for distance") {
        Ruler.localize(Locale.CANADA)

        Distance.ofCentimeters(100).format() shouldBe "1.00m"
      }

      it("uses yards as default imperial units for distance") {
        Distance.ofYards(1).format() shouldBe "1yd"
      }

      it("uses a supplied unit separator") {
        val context = FormattingContext.withSeparator("|")
        Length.ofInches(1).format(context) shouldBe "1|in"
      }

      it("uses a supplied fractional precision") {
        val context = FormattingContext.withPrecision(3)
        Length.ofMeters(1).format(context) shouldBe "1.000m"
      }

      it("uses a localised grouping") {
        val length = Length.ofKilometers(1_523_256)

        length.format() shouldBe "1,523,256.00km"

        Ruler.localize(Locale("pl"))

        length.format() shouldBe "1 523 256,00km"
      }

      it("uses a supplied length converter") {
        val converter = LengthConverter { Distance.Zero.toLength(Meter) }

        Distance.Max.format(converter = converter) shouldBe "0.00m"
      }

      it("uses a supplied length formatter") {
        val formatter = LengthFormatter { _, _ -> "hello" }

        Distance.Zero.format(formatter = formatter) shouldBe "hello"
      }
    }
  }
})
