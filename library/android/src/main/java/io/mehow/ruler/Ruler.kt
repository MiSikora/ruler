package io.mehow.ruler

import android.content.Context
import io.mehow.ruler.SiLengthUnit.Meter

object Ruler {
  private val converters = mutableListOf<LengthConverter>()

  @JvmStatic
  @JvmName("addConverter")
  operator fun plusAssign(converter: LengthConverter) = synchronized(this) {
    converters += converter
  }

  @JvmStatic
  @JvmName("removeConverter")
  operator fun minusAssign(converter: LengthConverter) = synchronized(this) {
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

  @JvmStatic
  @JvmName("addFormatter")
  operator fun plusAssign(formatter: LengthFormatter) = synchronized(this) {
    formatters += formatter
  }

  @JvmStatic
  @JvmName("removeFormatter")
  operator fun minusAssign(formatter: LengthFormatter) = synchronized(this) {
    formatters -= formatter
  }

  internal val formatter = object : LengthFormatter {
    override fun Length<*>.format(context: Context, separator: String): String? {
      return formatters.toList()
          .asSequence()
          .map { format(context, separator) }
          .firstOrNull() ?: with(ResourcesLengthFormatter) { format(context, separator) }
    }
  }

  @JvmStatic
  @JvmOverloads
  fun format(
    distance: Distance,
    context: Context,
    separator: String = "",
    converter: LengthConverter? = Ruler.converter,
    formatter: LengthFormatter = Ruler.formatter
  ): String {
    return distance.format(context, separator, converter, formatter)
  }

  @JvmStatic
  @JvmOverloads
  fun <T> format(
    length: Length<T>,
    context: Context,
    separator: String = "",
    converter: LengthConverter? = Ruler.converter,
    formatter: LengthFormatter = Ruler.formatter
  ): String where T : LengthUnit, T : Comparable<T>, T : Iterable<T> {
    return length.format(context, separator, converter, formatter)
  }
}

@JvmSynthetic
fun Distance.format(
  context: Context,
  separator: String = "",
  converter: LengthConverter? = Ruler.converter,
  formatter: LengthFormatter = Ruler.formatter
): String {
  return toLength(Meter).format(context, separator, converter, formatter)
}

@JvmSynthetic
fun <T> Length<T>.format(
  context: Context,
  separator: String = "",
  converter: LengthConverter? = Ruler.converter,
  formatter: LengthFormatter = Ruler.formatter
): String where T : LengthUnit, T : Comparable<T>, T : Iterable<T> {
  val length = if (converter == null) this else {
    val convertedLength = with(converter) { convert(context) }
    checkNotNull(convertedLength) { "Failed to convert length $convertedLength." }
  }

  val text = with(formatter) { length.format(context, separator) }
  checkNotNull(text) { "Failed to format length $length." }

  return text
}
