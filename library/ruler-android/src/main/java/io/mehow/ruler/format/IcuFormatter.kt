package io.mehow.ruler.format

import android.icu.number.NumberFormatter
import android.icu.number.Precision
import android.icu.text.DecimalFormat
import android.os.Build.VERSION
import io.mehow.ruler.LengthUnit
import io.mehow.ruler.Measure

/**
 * Formatter that relies on the available [ICU](http://site.icu-project.org/home) to shape the output.
 */
public object IcuFormatter : MeasureFormatter {
  override fun format(
    measure: Measure<LengthUnit<*>>,
    context: MeasureContext,
  ): String = when {
    VERSION.SDK_INT >= 30 -> NumberFormatter.withLocale(context.locale)
        .precision(Precision.fixedFraction(context.fractionalPrecision))
        .format(measure.value)
        .toString()
    VERSION.SDK_INT >= 24 -> DecimalFormat.getInstance(context.locale).apply {
      minimumIntegerDigits = 1
      minimumFractionDigits = context.fractionalPrecision
      maximumFractionDigits = context.fractionalPrecision
    }.format(measure.value)
    else -> DecimalFormatter.format(measure, context)
  }
}
