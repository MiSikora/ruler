package io.mehow.ruler

import android.content.Context

/**
 * Formats [Length] to a displayable output. Can be automatically applied during data formatting
 * by [installing][Ruler.addFormatter] it into the [Ruler].
 */
public fun interface LengthFormatter {
  /**
   * Formats an input [Length] to a human-friendly text using a [separator] between a numeric value and unit.
   * Returns `null` if an input handling should be delegated to a different formatter.
   */
  public fun Length<*>.format(context: Context, separator: String): String?
}
