package io.mehow.ruler.format

import io.mehow.ruler.Distance
import io.mehow.ruler.ImperialLengthUnit.Foot
import io.mehow.ruler.ImperialLengthUnit.Inch
import io.mehow.ruler.ImperialLengthUnit.Mile
import io.mehow.ruler.ImperialLengthUnit.Yard
import io.mehow.ruler.Length
import io.mehow.ruler.LengthUnit
import io.mehow.ruler.Ruler
import io.mehow.ruler.SiLengthUnit.Centimeter
import io.mehow.ruler.SiLengthUnit.Decameter
import io.mehow.ruler.SiLengthUnit.Decimeter
import io.mehow.ruler.SiLengthUnit.Gigameter
import io.mehow.ruler.SiLengthUnit.Hectometer
import io.mehow.ruler.SiLengthUnit.Kilometer
import io.mehow.ruler.SiLengthUnit.Megameter
import io.mehow.ruler.SiLengthUnit.Meter
import io.mehow.ruler.SiLengthUnit.Micrometer
import io.mehow.ruler.SiLengthUnit.Millimeter
import io.mehow.ruler.SiLengthUnit.Nanometer
import java.util.Locale

/**
 * Provides an APIs for managing [Length] formatting.
 */
public class FormattingDriver internal constructor(
  private val builder: Builder,
) {
  /**
   * Properties that should be used for shaping a formatted [Length].
   */
  public val formattingContext: FormattingContext = builder.formattingContext
  private val translator = builder.translator
  private val measureFormatter = builder.measureFormatter

  internal fun toLocalizedLength(distance: Distance) = when (translator.locale.country) {
    in Ruler.installedImperialCountryCodes -> distance.toLength(Yard)
    else -> distance.toLength(Meter)
  }

  /**
   * Formats length to a human-readable form using a supplied context.
   */
  public fun format(length: Length<*>, context: FormattingContext = formattingContext): String {
    val value = measureFormatter.format(length.measure, MeasureContext(context.fractionalPrecision, translator.locale))
    val separator = context.unitSeparator
    val unit = translator.symbol(length.unit)
    return value + separator + unit
  }

  /**
   * Returns a builder whose build invocation returns a driver with the same configuration as this one.
   */
  public fun newBuilder(): Builder = builder

  /**
   * [FormattingDriver] builder.
   */
  public class Builder internal constructor(
    internal val translator: Translator,
    internal val measureFormatter: MeasureFormatter,
    internal val formattingContext: FormattingContext,
  ) {
    public constructor() : this(
        translator = usTranslator,
        measureFormatter = DecimalFormatter,
        formattingContext = defaultFormattingContext,
    )

    /**
     * Sets translator that should be used by formatting APIs.
     */
    public fun withTranslator(translator: Translator): Builder = Builder(
        translator = translator,
        measureFormatter = measureFormatter,
        formattingContext = formattingContext,
    )

    /**
     * Sets formatter that should be used to shape length's [raw value][Length.measure].
     */
    public fun withMeasureFormatter(formatter: MeasureFormatter): Builder = Builder(
        translator = translator,
        measureFormatter = formatter,
        formattingContext = formattingContext,
    )

    /**
     * Sets properties that should be used by formatting APIs.
     */
    public fun withFormattingContext(context: FormattingContext): Builder = Builder(
        translator = translator,
        measureFormatter = measureFormatter,
        formattingContext = context,
    )

    /**
     * Creates a new driver with properties defined in this builder.
     */
    public fun build(): FormattingDriver = FormattingDriver(this)
  }

  private companion object {
    private val usTranslator = object : Translator {
      override val locale: Locale = Locale.US

      override fun symbol(unit: LengthUnit<*>): String = when (unit) {
        Nanometer -> "nm"
        Micrometer -> "µm"
        Millimeter -> "mm"
        Centimeter -> "cm"
        Decimeter -> "dm"
        Meter -> "m"
        Decameter -> "dam"
        Hectometer -> "hm"
        Kilometer -> "km"
        Megameter -> "Mm"
        Gigameter -> "Gm"
        Inch -> "in"
        Foot -> "ft"
        Yard -> "yd"
        Mile -> "mi"
      }
    }

    private val defaultFormattingContext = FormattingContext.Builder().build()
  }
}
