package io.mehow.ruler

import android.content.Context

/**
 * Formatter that applies a resource based on a unit type in [Length].
 */
public object AutoLengthFormatter : LengthFormatter {
  override fun Length<*>.format(
    unitSeparator: String,
    context: Context,
  ): String = context.getString(
      R.string.io_mehow_ruler_format_pattern,
      measure.value.format(context.preferredLocale, precision = 2),
      unitSeparator,
      context.getString(unit.resource),
  )
}
