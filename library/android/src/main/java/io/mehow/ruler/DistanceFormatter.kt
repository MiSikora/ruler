@file:JvmName("DistanceFormatter")

package io.mehow.ruler

import android.content.Context
import io.mehow.ruler.SiLengthUnit.Meter

@JvmOverloads fun Distance.formatLocalized(
  context: Context,
  unitSeparator: String = "",
  resourceProvider: UnitResourceProvider<LengthUnit> = BuiltInLengthUnitProvider
): String {
  return toLength(Meter).formatLocalized(context, unitSeparator, resourceProvider, BuiltInConverter)
}
