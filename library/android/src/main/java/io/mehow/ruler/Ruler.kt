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

  internal val flooredFormatters = mutableListOf<LengthFormatter>()

  public fun addFlooredFormatter(formatter: LengthFormatter): Unit = synchronized(flooredFormatters) {
    flooredFormatters += formatter
  }

  public fun removeFlooredFormatter(formatter: LengthFormatter): Unit = synchronized(flooredFormatters) {
    flooredFormatters -= formatter
  }

  internal val flooredFormatter = object : LengthFormatter {
    override fun Length<*>.format(context: Context, separator: String): String? = flooredFormatters.toList()
        .asSequence()
        .map { format(context, separator) }
        .firstOrNull() ?: with(FlooredLengthFormatter) { format(context, separator) }
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
public fun <T> Distance.format(
  context: Context,
  unit: T,
  separator: String = "",
  converter: LengthConverter? = Ruler.converter,
  formatter: LengthFormatter = Ruler.formatter,
): String where T : Enum<T>, T : LengthUnit<T> = toLength(unit).format(context, separator, converter, formatter)

public fun Distance.formatFloored(
  context: Context,
  separator: String = "",
): String = when {
  context.useImperialUnits -> formatFloored(context, Yard, separator)
  else -> formatFloored(context, Meter, separator)
}

public fun <T> Distance.formatFloored(
  context: Context,
  unit: T,
  separator: String = "",
): String where T : Enum<T>, T : LengthUnit<T> = toLength(unit).formatFloored(context, separator)

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

public fun Length<*>.formatFloored(
  context: Context,
  separator: String = "",
): String = format(context, separator, null, Ruler.flooredFormatter)
