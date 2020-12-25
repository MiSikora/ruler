@file:JvmName("DistanceUtils")

package io.mehow.ruler

import android.content.Context
import io.mehow.ruler.ImperialLengthUnit.Yard
import io.mehow.ruler.SiLengthUnit.Meter

@JvmOverloads public fun Distance.format(
  context: Context,
  separator: String = "",
  converter: LengthConverter? = Ruler.converter,
  formatter: LengthFormatter = Ruler.formatter,
): String {
  val useImperial = context.preferredLocale.isImperial
  return if (useImperial) format(context, Yard, separator, converter, formatter)
  else format(context, Meter, separator, converter, formatter)
}

@Suppress("LongParameterList")
@JvmOverloads
public fun <T> Distance.format(
  context: Context,
  unit: T,
  separator: String = "",
  converter: LengthConverter? = Ruler.converter,
  formatter: LengthFormatter = Ruler.formatter,
): String where T : Enum<T>, T : LengthUnit<T> {
  return toLength(unit).format(context, separator, converter, formatter)
}

@JvmOverloads public fun Distance.formatFloored(
  context: Context,
  separator: String = "",
): String {
  val useImperial = context.preferredLocale.isImperial
  return if (useImperial) formatFloored(context, Yard, separator)
  else formatFloored(context, Meter, separator)
}

@JvmOverloads public fun <T> Distance.formatFloored(
  context: Context,
  unit: T,
  separator: String = "",
): String where T : Enum<T>, T : LengthUnit<T> {
  return toLength(unit).formatFloored(context, separator)
}
