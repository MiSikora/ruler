package io.mehow.ruler

import android.content.Context
import io.mehow.ruler.ImperialLengthUnit.Yard
import io.mehow.ruler.SiLengthUnit.Meter

public object Ruler {
  internal val converters = mutableListOf<LengthConverter>()

  public fun addConverter(converter: LengthConverter): Unit = synchronized(this) {
    converters += converter
  }

  public fun removeConverter(converter: LengthConverter): Unit = synchronized(this) {
    converters -= converter
  }

  internal val converter = object : LengthConverter {
    override fun Length<*>.convert(context: Context): Length<*> = converters.toList()
        .asSequence()
        .map { convert(context) }
        .firstOrNull() ?: with(AutoFitLengthConverter) { convert(context) }
  }

  internal val formatters = mutableListOf<LengthFormatter>()

  public fun addFormatter(formatter: LengthFormatter): Unit = synchronized(this) {
    formatters += formatter
  }

  public fun removeFormatter(formatter: LengthFormatter): Unit = synchronized(this) {
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

  private val mutableImperialCountries = mutableSetOf("US", "LR", "MM")
  internal val imperialCountryCodes get() = mutableImperialCountries.toSet()

  private const val ukCountryCode = "GB"
  public var isUkImperial: Boolean
    get() = ukCountryCode in imperialCountryCodes
    set(value) {
      val func = if (value) mutableImperialCountries::add else mutableImperialCountries::remove
      synchronized(this) { func(ukCountryCode) }
    }

  internal val flooredFormatters = mutableListOf<LengthFormatter>()

  public fun addFlooredFormatter(formatter: LengthFormatter): Unit = synchronized(this) {
    flooredFormatters += formatter
  }

  public fun removeFlooredFormatter(formatter: LengthFormatter): Unit = synchronized(this) {
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
  val length = if (converter == null) this else {
    val convertedLength = with(converter) { convert(context) }
    checkNotNull(convertedLength) { "Failed to convert length: $this" }
  }

  val text = with(formatter) { length.format(context, separator) }
  return checkNotNull(text) { "Failed to format length: $length" }
}

public fun Length<*>.formatFloored(
  context: Context,
  separator: String = "",
): String = format(context, separator, null, Ruler.flooredFormatter)
