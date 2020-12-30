package io.mehow.ruler

import android.content.Context
import io.mehow.ruler.ImperialLengthUnit.Yard
import io.mehow.ruler.SiLengthUnit.Meter

/**
 * Formatter that applies whole quantity of units in a [Length].
 */
public object FlooredLengthFormatter : LengthFormatter {
  override fun Length<*>.format(
    unitSeparator: String,
    context: Context,
  ): String = context.getString(
      R.string.io_mehow_ruler_format_pattern,
      measure.value.toBigInteger().format(context.preferredLocale, precision = 0),
      unitSeparator,
      context.getString(unit.resource),
  )
}

/**
 * Formats this distance to a human readable form. Uses [Yard] or [Meter] as a base unit depending on a system locale.
 * Amount of units in the output is floored.
 *
 * @param context Context that will be used for determining the [unit system][LengthUnit].
 * @param unitSeparator Separator that should be used between a numeric value and a unit.
 */
public fun Distance.formatFloored(
  context: Context,
  unitSeparator: String = "",
): String = when {
  context.useImperialUnits -> toLength(Yard)
  else -> toLength(Meter)
}.formatFloored(context, unitSeparator)

/**
 * Formats this length to a human readable form. Amount of units in the output is floored.
 *
 * @param context Context that will be used for determining the [unit system][LengthUnit].
 * @param unitSeparator Separator that should be used between a numeric value and a unit.
 */
public fun Length<*>.formatFloored(
  context: Context,
  unitSeparator: String = "",
): String = format(context, unitSeparator, converter = null, FlooredLengthFormatter)
