package io.mehow.ruler

import android.content.Context
import android.content.res.Configuration
import androidx.test.core.app.ApplicationProvider
import io.kotlintest.shouldBe
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.Locale

@RunWith(RobolectricTestRunner::class)
class UkAsImperialTest {
  private val context: Context get() = ApplicationProvider.getApplicationContext()

  @Test fun `UK can be set to use imperial units`() {
    // Only language of locale can be set with @Config and we need to set country.
    val config = context.resources.configuration
    val localizedConfig = Configuration(config).apply { setLocale(Locale.UK) }
    val localizedContext = context.createConfigurationContext(localizedConfig)

    val distance = Distance.ofFeet(4)
    distance.format(localizedContext) shouldBe "1.22m"

    Ruler.isUkImperial = true
    distance.format(localizedContext) shouldBe "1yd 1ft"

    Ruler.isUkImperial = false
    distance.format(localizedContext) shouldBe "1.22m"
  }
}