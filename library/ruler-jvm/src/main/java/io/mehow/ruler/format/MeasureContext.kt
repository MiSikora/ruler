package io.mehow.ruler.format

import java.util.Locale

/**
 * Properties that can be used to format a [Measure][io.mehow.ruler.Measure].
 */
public class MeasureContext internal constructor(
  /**
   * Amount of digits that should be shown after a decimal point of a formatted measure.
   */
  public val fractionalPrecision: Int,
  /**
   * Locale that dictates which grouping properties should be used when formatting a measure.
   */
  public val locale: Locale,
)
