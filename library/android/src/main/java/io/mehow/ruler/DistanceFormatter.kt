@file:JvmName("DistanceFormatter")

package io.mehow.ruler

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import io.mehow.ruler.ImperialDistanceUnit.Yard
import io.mehow.ruler.SiDistanceUnit.Meter
import java.util.Locale

@JvmOverloads fun <T> Distance<T>.format(
  context: Context,
  unitSeparator: String = "",
  resourceProvider: DistanceUnitResourceProvider<DistanceUnit> = BuiltInDistanceUnitProvider,
  converter: DistanceConverter? = BuiltInConverter
): String where T : DistanceUnit, T : Comparable<T>, T : Iterable<T> {
  val value = converter?.convert(this) ?: this
  val resource = resourceProvider.resource(value.unit)
  val distance = value.measuredLength.toDouble()
  return context.getString(resource, distance, unitSeparator)
}

@Suppress("LongParameterList")
@JvmOverloads fun <T> Distance<T>.formatLocalized(
  context: Context,
  unitSeparator: String = "",
  resourceProvider: DistanceUnitResourceProvider<DistanceUnit> = BuiltInDistanceUnitProvider,
  converter: DistanceConverter? = BuiltInConverter,
  locale: Locale? = null
): String where T : DistanceUnit, T : Comparable<T>, T : Iterable<T> {
  val preferredContext = if (locale == null) context else context.toLocalizedContext(locale)
  val preferredLocale = preferredContext.preferredLocale
  val localizedDistance = if (preferredLocale.isImperial) withUnit(Yard) else withUnit(Meter)
  return localizedDistance.format(preferredContext, unitSeparator, resourceProvider, converter)
}

private val Context.preferredLocale: Locale
  get() {
    val configuration = resources.configuration
    return if (Build.VERSION.SDK_INT >= 24) configuration.locales[0] else configuration.locale
  }

private fun Context.toLocalizedContext(locale: Locale): Context {
  val config = resources.configuration
  val localizedConfig = Configuration(config).apply {
    setLocale(locale)
  }
  return createConfigurationContext(localizedConfig)
}

private val Locale.isImperial
  get() = when (country) {
    "US", "LR", "MM" -> true
    else -> false
  }
