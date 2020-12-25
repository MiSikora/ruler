package io.mehow.ruler

import android.content.Context

public object Ruler {
  internal val converters = mutableListOf<LengthConverter>()

  @JvmStatic public fun addConverter(converter: LengthConverter): Unit = synchronized(this) {
    converters += converter
  }

  @JvmStatic public fun removeConverter(converter: LengthConverter): Unit = synchronized(this) {
    converters -= converter
  }

  internal val converter = object : LengthConverter {
    override fun Length<*>.convert(context: Context): Length<*>? {
      return converters.toList()
          .asSequence()
          .map { convert(context) }
          .firstOrNull() ?: with(AutoFitLengthConverter) { convert(context) }
    }
  }

  internal val formatters = mutableListOf<LengthFormatter>()

  @JvmStatic public fun addFormatter(formatter: LengthFormatter): Unit = synchronized(this) {
    formatters += formatter
  }

  @JvmStatic public fun removeFormatter(formatter: LengthFormatter): Unit = synchronized(this) {
    formatters -= formatter
  }

  internal val formatter = object : LengthFormatter {
    override fun Length<*>.format(context: Context, separator: String): String? {
      return formatters.toList()
          .asSequence()
          .map { format(context, separator) }
          .firstOrNull() ?: with(AutoLengthFormatter) { format(context, separator) }
    }
  }

  @JvmStatic public var useImperialFormatter: Boolean
    get() = AutoLengthFormatter.useImperialFormatter
    set(value) {
      AutoLengthFormatter.useImperialFormatter = value
    }

  private val mutableImperialCountries = mutableSetOf("US", "LR", "MM")
  internal val imperialCountryCodes get() = mutableImperialCountries.toSet()

  private const val ukCountryCode = "GB"
  @JvmStatic public var isUkImperial: Boolean
    get() = ukCountryCode in imperialCountryCodes
    set(value) {
      val func = if (value) mutableImperialCountries::add else mutableImperialCountries::remove
      synchronized(this) { func(ukCountryCode) }
    }

  internal val flooredFormatters = mutableListOf<LengthFormatter>()

  @JvmStatic public fun addFlooredFormatter(formatter: LengthFormatter): Unit = synchronized(this) {
    flooredFormatters += formatter
  }

  @JvmStatic public fun removeFlooredFormatter(formatter: LengthFormatter): Unit = synchronized(this) {
    flooredFormatters -= formatter
  }

  internal val flooredFormatter = object : LengthFormatter {
    override fun Length<*>.format(context: Context, separator: String): String? {
      return flooredFormatters.toList()
          .asSequence()
          .map { format(context, separator) }
          .firstOrNull() ?: with(FlooredLengthFormatter) { format(context, separator) }
    }
  }
}
