package io.mehow.ruler.test

import android.content.Context
import android.content.res.Configuration
import androidx.test.core.app.ApplicationProvider
import java.util.Locale

internal fun getApplicationContext(locale: Locale? = null) = ApplicationProvider
    .getApplicationContext<Context>()
    .let { if (locale != null) it.localise(locale) else it }

internal fun Context.localise(locale: Locale): Context {
  val config = resources.configuration
  val localizedConfig = Configuration(config).apply { setLocale(locale) }
  return createConfigurationContext(localizedConfig)
}
