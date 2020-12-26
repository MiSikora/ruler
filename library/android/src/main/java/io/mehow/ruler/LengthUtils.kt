@file:JvmName("LengthUtils")

package io.mehow.ruler

import android.content.Context

@JvmOverloads public fun Length<*>.format(
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
  checkNotNull(text) { "Failed to format length: $length" }

  return text
}

@JvmOverloads public fun Length<*>.formatFloored(
  context: Context,
  separator: String = "",
): String = format(context, separator, null, Ruler.flooredFormatter)
