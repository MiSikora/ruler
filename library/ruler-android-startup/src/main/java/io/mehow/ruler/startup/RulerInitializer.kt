package io.mehow.ruler.startup

import android.content.Context
import androidx.startup.Initializer
import io.mehow.ruler.Ruler
import io.mehow.ruler.format.FormattingDriver
import io.mehow.ruler.format.withAndroidContext

public object RulerInitializer : Initializer<FormattingDriver> {
  override fun create(context: Context): FormattingDriver = FormattingDriver.Builder()
      .withAndroidContext(context)
      .build()
      .also { Ruler.driver = it }

  override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
