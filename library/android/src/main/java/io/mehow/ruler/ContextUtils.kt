package io.mehow.ruler

import android.content.Context
import android.os.Build.VERSION
import java.util.Locale

@Suppress("DEPRECATION")
internal val Context.preferredLocale: Locale
  get() {
    val configuration = resources.configuration
    return if (VERSION.SDK_INT >= 24) configuration.locales[0] else configuration.locale
  }
