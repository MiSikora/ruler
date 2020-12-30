package io.mehow.ruler

import android.icu.number.Precision
import android.os.Build
import java.util.Locale
import android.icu.number.NumberFormatter as AndroidNumberFormatter
import android.icu.text.DecimalFormat as AndroidDecimalFormatter
import java.text.DecimalFormat as JavaDecimalFormatter

internal fun Number.format(locale: Locale, precision: Int): String = when {
  Build.VERSION.SDK_INT >= 30 -> AndroidNumberFormatter.withLocale(locale)
      .precision(Precision.fixedFraction(precision))
      .format(this)
      .toString()
  Build.VERSION.SDK_INT >= 24 -> AndroidDecimalFormatter.getInstance(locale).apply {
    minimumIntegerDigits = 1
    minimumFractionDigits = precision
    maximumFractionDigits = precision
  }.format(this)
  else -> JavaDecimalFormatter.getInstance(locale).apply {
    minimumIntegerDigits = 1
    minimumFractionDigits = precision
    maximumFractionDigits = precision
  }.format(this)
}
