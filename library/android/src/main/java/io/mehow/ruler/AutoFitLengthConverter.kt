package io.mehow.ruler

import android.content.Context
import android.os.Build.VERSION
import io.mehow.ruler.ImperialLengthUnit.Yard
import io.mehow.ruler.SiLengthUnit.Kilometer
import io.mehow.ruler.SiLengthUnit.Meter
import java.util.Locale

internal object AutoFitLengthConverter : LengthConverter {
  @Suppress("UNCHECKED_CAST", "UseIfInsteadOfWhen")
  override fun Length<*>.convert(context: Context): Length<*>? {
    val length = if (context.preferredLocale.isImperial) withUnit(Yard) else withUnit(Meter)
    val autoLength = length.withAutoUnit()
    return when (autoLength.unit) {
      is SiLengthUnit -> (autoLength as Length<SiLengthUnit>).coerceUnitIn(Meter, Kilometer)
      else -> autoLength
    }
  }

  private val Context.preferredLocale: Locale
    get() {
      val configuration = resources.configuration
      return if (VERSION.SDK_INT >= 24) configuration.locales[0] else configuration.locale
    }

  private val imperialCountryCodes = listOf("US", "LR", "MM")
  private val Locale.isImperial get() = country in imperialCountryCodes
}
