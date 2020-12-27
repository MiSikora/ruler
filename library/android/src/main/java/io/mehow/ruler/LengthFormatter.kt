package io.mehow.ruler

import android.content.Context

/**
 * Formats [Length] to a displayable output.
 */
public fun interface LengthFormatter {
  /**
   * Formats an input [Length] to a human-friendly text using a separator between a numeric value and a unit.
   * Returns `null` if an input handling should be delegated to a different formatter.
   */
  public fun Length<*>.format(unitSeparator: String, context: Context): String?

  /**
   * Factory for [LengthFormatter] that can be [installed][Ruler.addFormatterFactory] in [Ruler].
   */
  public fun interface Factory {
    /**
     * Creates a [LengthFormatter] that will be used to format an input. It should return `null` if no formatter
     * created by this factory can handle the input.
     */
    public fun create(length: Length<*>, unitSeparator: String, context: Context): LengthFormatter?
  }
}
