package io.mehow.ruler.format

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mehow.ruler.Distance
import io.mehow.ruler.ImperialLengthUnit.Mile
import io.mehow.ruler.SiLengthUnit.Meter

internal class ImperialFormatterSpec : DescribeSpec({
  describe("formatter") {
    it("displays all unit parts") {
      val distance = Distance.ofMiles(10) +
          Distance.ofYards(11) +
          Distance.ofFeet(2) +
          Distance.ofInches(7)
      val length = distance.toLength(Meter)
      val formatter = ImperialFormatter.Full

      val formattedLength = length.format(converter = null, formatter = formatter)

      formattedLength shouldBe "10mi 11yd 2ft 7in"
    }

    it("displays negative lengths") {
      val distance = -(Distance.ofMiles(10) +
          Distance.ofYards(11) +
          Distance.ofFeet(2) +
          Distance.ofInches(7))
      val length = distance.toLength(Meter)
      val formatter = ImperialFormatter.Full

      val formattedLength = length.format(converter = null, formatter = formatter)

      formattedLength shouldBe "-10mi 11yd 2ft 7in"
    }

    it("ignores zero unit parts") {
      val distance = Distance.ofMiles(10)
      val length = distance.toLength(Meter)
      val formatter = ImperialFormatter.Full

      val formattedLength = length.format(converter = null, formatter = formatter)

      formattedLength shouldBe "10mi"
    }

    it("displays zero miles length") {
      val distance = Distance.Zero
      val length = distance.toLength(Meter)
      val formatter = ImperialFormatter.Builder()
          .withMiles()
          .build()

      val formattedLength = length.format(converter = null, formatter = formatter)

      formattedLength shouldBe "0mi"
    }

    it("displays zero yards length") {
      val distance = Distance.Zero
      val length = distance.toLength(Meter)
      val formatter = ImperialFormatter.Builder()
          .withYards()
          .build()

      val formattedLength = length.format(converter = null, formatter = formatter)

      formattedLength shouldBe "0yd"
    }

    it("displays zero feet length") {
      val distance = Distance.Zero
      val length = distance.toLength(Meter)
      val formatter = ImperialFormatter.Builder()
          .withFeet()
          .build()

      val formattedLength = length.format(converter = null, formatter = formatter)

      formattedLength shouldBe "0ft"
    }

    it("displays zero inches length") {
      val distance = Distance.Zero
      val length = distance.toLength(Meter)
      val formatter = ImperialFormatter.Builder()
          .withInches()
          .build()

      val formattedLength = length.format(converter = null, formatter = formatter)

      formattedLength shouldBe "0in"
    }

    it("displays only smallest unit part") {
      val distance = Distance.Zero
      val length = distance.toLength(Meter)
      val formatter = ImperialFormatter.Full

      val formattedLength = length.format(converter = null, formatter = formatter)

      formattedLength shouldBe "0in"
    }

    it("accumulates miles for yards") {
      val distance = Distance.ofMiles(2) + Distance.ofYards(5)
      val length = distance.toLength(Meter)
      val formatter = ImperialFormatter.Builder()
          .withYards()
          .build()

      val formattedLength = length.format(converter = null, formatter = formatter)

      formattedLength shouldBe "3,525yd"
    }

    it("accumulates miles and yards for feet") {
      val distance = Distance.ofMiles(2) +
          Distance.ofYards(5) +
          Distance.ofFeet(3)
      val length = distance.toLength(Meter)
      val formatter = ImperialFormatter.Builder()
          .withFeet()
          .build()

      val formattedLength = length.format(converter = null, formatter = formatter)

      formattedLength shouldBe "10,578ft"
    }

    it("accumulates miles, yards and feet for inches") {
      val distance = Distance.ofMiles(2) +
          Distance.ofYards(5) +
          Distance.ofFeet(3) +
          Distance.ofInches(11)
      val length = distance.toLength(Meter)
      val formatter = ImperialFormatter.Builder()
          .withInches()
          .build()

      val formattedLength = length.format(converter = null, formatter = formatter)

      formattedLength shouldBe "126,947in"
    }

    it("rounds down lengths for miles") {
      val distance = Distance.ofYards(300) +
          Distance.ofFeet(2) +
          Distance.ofInches(11)
      val length = distance.toLength(Meter)
      val formatter = ImperialFormatter.Builder()
          .withMiles()
          .build()

      val formattedLength = length.format(converter = null, formatter = formatter)

      formattedLength shouldBe "0mi"
    }

    it("rounds down lengths for yards") {
      val distance = Distance.ofFeet(1) +
          Distance.ofInches(5)
      val length = distance.toLength(Meter)
      val formatter = ImperialFormatter.Builder()
          .withYards()
          .build()

      val formattedLength = length.format(converter = null, formatter = formatter)

      formattedLength shouldBe "0yd"
    }

    it("rounds down lengths for feet") {
      val distance = Distance.ofInches(4)
      val length = distance.toLength(Meter)
      val formatter = ImperialFormatter.Builder()
          .withFeet()
          .build()

      val formattedLength = length.format(converter = null, formatter = formatter)

      formattedLength shouldBe "0ft"
    }

    it("rounds up lengths for miles") {
      val distance = Distance.ofYards(973) +
          Distance.ofFeet(2) +
          Distance.ofInches(11)
      val length = distance.toLength(Meter)
      val formatter = ImperialFormatter.Builder()
          .withMiles()
          .build()

      val formattedLength = length.format(converter = null, formatter = formatter)

      formattedLength shouldBe "1mi"
    }

    it("rounds up lengths for yards") {
      val distance = Distance.ofFeet(2) + Distance.ofInches(5)
      val length = distance.toLength(Meter)
      val formatter = ImperialFormatter.Builder()
          .withYards()
          .build()

      val formattedLength = length.format(converter = null, formatter = formatter)

      formattedLength shouldBe "1yd"
    }

    it("rounds up lengths for feet") {
      val distance = Distance.ofInches(8)
      val length = distance.toLength(Meter)
      val formatter = ImperialFormatter.Builder()
          .withFeet()
          .build()

      val formattedLength = length.format(converter = null, formatter = formatter)

      formattedLength shouldBe "1ft"
    }

    it("has a default parts fallback") {
      val distance = Distance.ofMiles(10) +
          Distance.ofYards(11) +
          Distance.ofFeet(2) +
          Distance.ofInches(7)
      val length = distance.toLength(Meter)
      val formatter = ImperialFormatter.Builder().build()

      val formattedLength = length.format(converter = null, formatter = formatter)

      formattedLength shouldBe "17,612yd"
    }

    it("uses a custom parts fallback") {
      val distance = Distance.ofMiles(10) +
          Distance.ofYards(11) +
          Distance.ofFeet(2) +
          Distance.ofInches(7)
      val length = distance.toLength(Meter)
      val formatter = ImperialFormatter.Builder().withFallbackUnit(Mile).build()

      val formattedLength = length.format(converter = null, formatter = formatter)

      formattedLength shouldBe "10mi"
    }

    it("displays custom unit separator in a correct position") {
      val distance = Distance.ofMiles(4) +
          Distance.ofYards(3) +
          Distance.ofFeet(2) +
          Distance.ofInches(1)
      val length = distance.toLength(Meter)
      val formatter = ImperialFormatter.Full

      val context = FormattingContext.withSeparator("|")
      val formattedLength = length.format(context, converter = null, formatter = formatter)

      formattedLength shouldBe "4|mi 3|yd 2|ft 1|in"
    }

    it("displays custom part separator in a correct position") {
      val distance = Distance.ofMiles(4) +
          Distance.ofYards(3) +
          Distance.ofFeet(2) +
          Distance.ofInches(1)
      val length = distance.toLength(Meter)
      val formatter = ImperialFormatter.Builder()
          .withMiles()
          .withYards()
          .withFeet()
          .withInches()
          .withPartSeparator("|")
          .build()

      val formattedLength = length.format(converter = null, formatter = formatter)

      formattedLength shouldBe "4mi|3yd|2ft|1in"
    }
  }
})
