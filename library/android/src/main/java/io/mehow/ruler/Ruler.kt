package io.mehow.ruler

import android.content.Context

object Ruler {
  private val converters = mutableListOf<LengthConverter>()

  @JvmStatic fun addConverter(converter: LengthConverter) = synchronized(this) {
    converters += converter
  }

  @JvmStatic fun removeConverter(converter: LengthConverter) = synchronized(this) {
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

  private val formatters = mutableListOf<LengthFormatter>()

  @JvmStatic fun addFormatter(formatter: LengthFormatter) = synchronized(this) {
    formatters += formatter
  }

  @JvmStatic fun removeFormatter(formatter: LengthFormatter) = synchronized(this) {
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

  @JvmStatic var isImperialAutoFormattingEnabled
    get() = AutoLengthFormatter.useImperialFormatter
    set(value) {
      AutoLengthFormatter.useImperialFormatter = value
    }

  private val mutableImperialCountries = mutableSetOf("US", "LR", "MM")
  internal val imperialCountryCodes get() = mutableImperialCountries.toSet()

  private const val ukCountryCode = "GB"
  @JvmStatic var isUkImperial
    get() = ukCountryCode in imperialCountryCodes
    set(value) {
      val func = if (value) mutableImperialCountries::add else mutableImperialCountries::remove
      synchronized(this) { func(ukCountryCode) }
    }

  private val flooredFormatters = mutableListOf<LengthFormatter>()

  @JvmStatic fun addFlooredFormatter(formatter: LengthFormatter) = synchronized(this) {
    flooredFormatters += formatter
  }

  @JvmStatic fun removeFlooredFormatter(formatter: LengthFormatter) = synchronized(this) {
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
