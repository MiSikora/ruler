package io.mehow.ruler

import android.content.Context
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

  private val imperialCountryCodes = listOf("US", "LR", "MM")
  private val Locale.isImperial get() = country in imperialCountryCodes
}
