package io.mehow.ruler.startup

import android.content.Context
import androidx.startup.Initializer
import io.mehow.ruler.Ruler
import io.mehow.ruler.format.FormattingDriver
import io.mehow.ruler.format.withAndroidContext

public object RulerInitializer : Initializer<FormattingDriver> {
  override fun create(context: Context): FormattingDriver {
    val driver = FormattingDriver.Builder()
        .withAndroidContext(context)
        .build()
    Ruler.driver = driver
    return driver
  }

  override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
