package io.mehow.ruler

import android.content.Context
import android.os.Build.VERSION
import java.util.Locale

internal val Context.preferredLocale: Locale
  get() = when {
    VERSION.SDK_INT >= 24 -> resources.configuration.locales[0]
    else -> @Suppress("DEPRECATION") resources.configuration.locale
  }

internal val Context.useImperialUnits get() = preferredLocale.country in Ruler.imperialCountryCodes
