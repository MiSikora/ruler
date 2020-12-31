package io.mehow.ruler.format

import io.mehow.ruler.LengthUnit
import io.mehow.ruler.Measure
import java.text.DecimalFormat

/**
 * Formatter that delegates its work to [DecimalFormat].
 */
public object DecimalFormatter : MeasureFormatter {
  override fun format(
    measure: Measure<LengthUnit<*>>,
    context: MeasureContext,
  ): String = DecimalFormat.getInstance(context.locale).apply {
    minimumIntegerDigits = 1
    minimumFractionDigits = context.fractionalPrecision
    maximumFractionDigits = context.fractionalPrecision
  }.format(measure.value)
}
