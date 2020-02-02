@file:JvmName("DistanceFormatter")

package io.mehow.ruler

import android.content.Context

@JvmOverloads fun <T> Distance<T>.format(
  context: Context,
  unitSeparator: String = "",
  resourceProvider: DistanceUnitResourceProvider<DistanceUnit> = LibraryDistanceUnitProvider
): String where T : DistanceUnit, T : Comparable<T>, T : Iterable<T> {
  val resource = resourceProvider.resource(unit)
  val distance = measuredLength.toDouble()
  return context.getString(resource, distance, unitSeparator)
}

@JvmOverloads fun <T> Distance<T>.coercedFormat(
  context: Context,
  unitSeparator: String = "",
  resourceProvider: DistanceUnitResourceProvider<DistanceUnit> = LibraryDistanceUnitProvider,
  coercer: DistanceUnitCoercer<T> = LibraryDistanceUnitCoercer()
): String where T : DistanceUnit, T : Comparable<T>, T : Iterable<T> {
  return coercer.coerce(this).format(context, unitSeparator, resourceProvider)
}
