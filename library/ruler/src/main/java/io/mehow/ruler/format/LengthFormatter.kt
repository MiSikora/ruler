package io.mehow.ruler.format

import io.mehow.ruler.Length

/**
 * Formats [Length] to a displayable output.
 */
public fun interface LengthFormatter {
  /**
   * Formats an input [Length] to a human-friendly text using a separator between a numeric value and a unit.
   */
  public fun format(length: Length<*>, driver: FormattingDriver): String

  /**
   * Factory for [LengthFormatter] that can be [installed][io.mehow.ruler.Ruler.addFormatterFactory]
   * in [io.mehow.ruler.Ruler].
   */
  public fun interface Factory {
    /**
     * Creates a [LengthFormatter] that will be used to format an input. It should return `null` if no formatter
     * created by this factory can handle the input.
     */
    public fun create(length: Length<*>, context: FormattingContext): LengthFormatter?
  }
}
