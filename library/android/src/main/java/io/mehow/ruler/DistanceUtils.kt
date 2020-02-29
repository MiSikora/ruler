@file:JvmName("DistanceUtils")

package io.mehow.ruler

import android.content.Context
import io.mehow.ruler.ImperialLengthUnit.Yard
import io.mehow.ruler.SiLengthUnit.Meter

@JvmOverloads fun Distance.format(
  context: Context,
  separator: String = "",
  converter: LengthConverter? = Ruler.converter,
  formatter: LengthFormatter = Ruler.formatter
): String {
  return toLength(Meter).format(context, separator, converter, formatter)
}

@JvmOverloads fun Distance.formatFloored(
  context: Context,
  separator: String = ""
): String {
  return if (context.preferredLocale.isImperial) formatFloored(context, Yard, separator)
  else formatFloored(context, Meter, separator)
}

@JvmOverloads fun <T> Distance.formatFloored(
  context: Context,
  unit: T,
  separator: String = ""
): String where T : LengthUnit, T : Comparable<T>, T : Iterable<T> {
  return toLength(unit).formatFloored(context, separator)
}
