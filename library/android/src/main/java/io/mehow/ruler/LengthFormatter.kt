@file:JvmName("LengthFormatter")

package io.mehow.ruler

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import io.mehow.ruler.ImperialLengthUnit.Yard
import io.mehow.ruler.SiLengthUnit.Meter
import java.util.Locale

@JvmOverloads fun <T> Length<T>.format(
  context: Context,
  unitSeparator: String = "",
  resourceProvider: UnitResourceProvider<LengthUnit> = BuiltInLengthUnitProvider,
  converter: LengthConverter? = BuiltInConverter
): String where T : LengthUnit, T : Comparable<T>, T : Iterable<T> {
  val value = converter?.convert(this) ?: this
  val resource = resourceProvider.resource(value.unit)
  return context.getString(resource, value.measuredLength, unitSeparator)
}

@Suppress("LongParameterList")
@JvmOverloads fun <T> Length<T>.formatLocalized(
  context: Context,
  unitSeparator: String = "",
  resourceProvider: UnitResourceProvider<LengthUnit> = BuiltInLengthUnitProvider,
  converter: LengthConverter? = BuiltInConverter,
  locale: Locale? = null
): String where T : LengthUnit, T : Comparable<T>, T : Iterable<T> {
  val preferredContext = if (locale == null) context else context.toLocalizedContext(locale)
  val preferredLocale = preferredContext.preferredLocale
  val localizedLength = if (preferredLocale.isImperial) withUnit(Yard) else withUnit(Meter)
  return localizedLength.format(preferredContext, unitSeparator, resourceProvider, converter)
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
