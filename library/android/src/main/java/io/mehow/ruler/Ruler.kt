package io.mehow.ruler

import android.content.Context
import io.mehow.ruler.ImperialLengthUnit.Yard
import io.mehow.ruler.SiLengthUnit.Meter

public object Ruler {
  internal val converters = mutableListOf<LengthConverter>()

  public fun addConverter(converter: LengthConverter): Unit = synchronized(converters) {
    converters += converter
  }

  public fun removeConverter(converter: LengthConverter): Unit = synchronized(converters) {
    converters -= converter
  }

  internal val converter = object : LengthConverter {
    override fun Length<*>.convert(context: Context): Length<*> = converters.toList()
        .asSequence()
        .map { convert(context) }
        .firstOrNull() ?: with(AutoFitLengthConverter) { convert(context) }
  }

  internal val formatters = mutableListOf<LengthFormatter>()

  public fun addFormatter(formatter: LengthFormatter): Unit = synchronized(formatters) {
    formatters += formatter
  }

  public fun removeFormatter(formatter: LengthFormatter): Unit = synchronized(formatters) {
    formatters -= formatter
  }

  internal val formatter = object : LengthFormatter {
    override fun Length<*>.format(context: Context, separator: String): String? = formatters.toList()
        .asSequence()
        .map { format(context, separator) }
        .firstOrNull() ?: with(AutoLengthFormatter) { format(context, separator) }
  }

  public var useImperialFormatter: Boolean
    get() = AutoLengthFormatter.useImperialFormatter
    set(value) {
      AutoLengthFormatter.useImperialFormatter = value
    }

  private val mutableImperialCountryCodes = mutableSetOf("US", "LR", "MM")
  internal val imperialCountryCodes get() = mutableImperialCountryCodes.toSet()

  private const val ukCountryCode = "GB"
  public var isUkImperial: Boolean
    get() = ukCountryCode in imperialCountryCodes
    set(add) {
      val func = if (add) mutableImperialCountryCodes::add else mutableImperialCountryCodes::remove
      synchronized(mutableImperialCountryCodes) { func(ukCountryCode) }
    }
}

public fun Distance.format(
  context: Context,
  separator: String = "",
  converter: LengthConverter? = Ruler.converter,
  formatter: LengthFormatter = Ruler.formatter,
): String = when {
  context.useImperialUnits -> format(context, Yard, separator, converter, formatter)
  else -> format(context, Meter, separator, converter, formatter)
}

@Suppress("LongParameterList")
public fun <T : LengthUnit<T>> Distance.format(
  context: Context,
  unit: T,
  separator: String = "",
  converter: LengthConverter? = Ruler.converter,
  formatter: LengthFormatter = Ruler.formatter,
): String = toLength(unit).format(context, separator, converter, formatter)

public fun Length<*>.format(
  context: Context,
  separator: String = "",
  converter: LengthConverter? = Ruler.converter,
  formatter: LengthFormatter = Ruler.formatter,
): String {
  val length = if (converter != null) {
    val convertedLength = with(converter) { convert(context) }
    checkNotNull(convertedLength) { "Failed to convert length: $this" }
  } else this

  val text = with(formatter) { length.format(context, separator) }
  return checkNotNull(text) { "Failed to format length: $length" }
}
