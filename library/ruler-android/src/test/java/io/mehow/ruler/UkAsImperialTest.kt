package io.mehow.ruler

import io.kotest.matchers.shouldBe
import io.mehow.ruler.test.ResetRulerRule
import io.mehow.ruler.test.getApplicationContext
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.Locale

@RunWith(RobolectricTestRunner::class)
internal class UkAsImperialTest {
  @get:Rule val resetRuler = ResetRulerRule
  private val context = getApplicationContext(Locale.UK)

  @Test fun `UK does not use imperial units by default`() {
    val distance = Distance.ofFeet(4)

    distance.format(context) shouldBe "1.22m"
  }

  @Test fun `UK can be switched on to use imperial units`() {
    val distance = Distance.ofFeet(4)

    Ruler.isUkImperial = true

    distance.format(context) shouldBe "1yd 1ft"
  }

  @Test fun `UK can be switched off to use imperial units`() {
    val distance = Distance.ofFeet(4)

    Ruler.isUkImperial = true
    Ruler.isUkImperial = false

    distance.format(context) shouldBe "1.22m"
  }
}
