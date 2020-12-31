package io.mehow.ruler.format

import io.mehow.ruler.LengthUnit
import io.mehow.ruler.Measure

/**
 * Formats [Measure] to a displayable output.
 */
public fun interface MeasureFormatter {
  /**
   * Formats, according to a supplied context, length's measure into a raw, localized number without any unit.
   */
  public fun format(measure: Measure<LengthUnit<*>>, context: MeasureContext): String
}
