package io.mehow.ruler

import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import io.mehow.ruler.SiLengthUnit.Meter
import io.mehow.ruler.test.ResetRulerRule
import io.mehow.ruler.test.getApplicationContext
import io.mehow.ruler.test.localise
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.Locale

@RunWith(RobolectricTestRunner::class)
internal class RulerTest {
  @get:Rule val resetRuler = ResetRulerRule
  private val context = getApplicationContext()

  @Test fun `has built-in converter factories`() {
    Ruler.installedConverterFactories.shouldNotBeEmpty()
  }

  @Test fun `cannot remove built-in converter factories`() {
    val factories = Ruler.installedConverterFactories

    factories.forEach(Ruler::removeConverterFactory)

    Ruler.installedConverterFactories shouldContainExactly factories
  }

  @Test fun `can add a converter factory`() {
    val factory = LengthConverter.Factory { _, _ -> null }

    Ruler.addConverterFactory(factory)

    Ruler.installedConverterFactories shouldContain factory
  }

  @Test fun `can remove a converter factory`() {
    val factory = LengthConverter.Factory { _, _ -> null }

    Ruler.addConverterFactory(factory)
    Ruler.removeConverterFactory(factory)

    Ruler.installedConverterFactories shouldNotContain factory
  }

  @Test fun `converter factories are used in the installed order`() {
    var text = ""
    Ruler.addConverterFactory { _, _ -> text += "a"; null }
    Ruler.addConverterFactory { _, _ -> text += "b"; null }

    Distance.Zero.format(context)

    text shouldBe "ab"
  }

  @Test fun `following converters are not used after first match`() {
    var text = ""
    Ruler.addConverterFactory { _, _ -> text += "a"; LengthConverter { this } }
    Ruler.addConverterFactory { _, _ -> text += "b"; null }

    Distance.Zero.format(context)

    text shouldBe "a"
  }

  @Test fun `has built-in formatter factories`() {
    Ruler.installedFormatterFactories.shouldNotBeEmpty()
  }

  @Test fun `cannot remove built-in formatter factories`() {
    val factories = Ruler.installedFormatterFactories

    factories.forEach(Ruler::removeFormatterFactory)

    Ruler.installedFormatterFactories shouldContainExactly factories
  }

  @Test fun `can add a formatter factory`() {
    val factory = LengthFormatter.Factory { _, _, _ -> null }

    Ruler.addFormatterFactory(factory)

    Ruler.installedFormatterFactories shouldContain factory
  }

  @Test fun `can remove a formatter factory`() {
    val factory = LengthFormatter.Factory { _, _, _ -> null }

    Ruler.addFormatterFactory(factory)
    Ruler.removeFormatterFactory(factory)

    Ruler.installedFormatterFactories shouldNotContain factory
  }

  @Test fun `formatter factories are used in the installed order`() {
    var text = ""
    Ruler.addFormatterFactory { _, _, _ -> text += "a"; null }
    Ruler.addFormatterFactory { _, _, _ -> text += "b"; null }

    Distance.Zero.format(context)

    text shouldBe "ab"
  }

  @Test fun `following formatters are not used after first match`() {
    var text = ""
    Ruler.addFormatterFactory { _, _, _ -> text += "a"; LengthFormatter { _, _ -> "" } }
    Ruler.addFormatterFactory { _, _, _ -> text += "b"; null }

    Distance.Zero.format(context)

    text shouldBe "a"
  }

  @Test fun `uses imperial formatting for imperial lengths`() {
    Length.ofFeet(1.5).format(context) shouldBe "1ft 6in"
  }

  @Test fun `does not use imperial formatting for SI lengths`() {
    Length.ofMeters(1).format(context) shouldBe "1.00m"
  }

  @Test fun `can turn off imperial formatting`() {
    Ruler.useImperialFormatter = false

    Length.ofFeet(1.5).format(context) shouldBe "1.50ft"
  }

  @Test fun `uses all units for imperial formatting`() {
    val length = Length.ofInches(1) +
        Length.ofFeet(2) +
        Length.ofYards(3) +
        Length.ofMiles(4)

    length.format(context) shouldBe "4mi 3yd 2ft 1in"
  }

  @Test fun `distance uses meter as a default SI unit`() {
    val context = context.localise(Locale("pl"))

    Distance.Zero.format(context) shouldBe "0,00m"
  }

  @Test fun `distance uses yard as a default imperial unit`() {
    Distance.ofYards(1).format(context) shouldBe "1yd"
  }

  @Test fun `uses a supplied unit separator`() {
    Length.ofInches(1).format(context, unitSeparator = "|") shouldBe "1|in"
  }

  @Test fun `uses a supplied length converter`() {
    val converter = LengthConverter { Distance.Zero.toLength(Meter) }

    Distance.Max.format(context, converter = converter) shouldBe "0.00m"
  }

  @Test fun `uses a supplied length formatter`() {
    val formatter = LengthFormatter { _, _ -> "hello" }

    Distance.Zero.format(context, formatter = formatter) shouldBe "hello"
  }
}
