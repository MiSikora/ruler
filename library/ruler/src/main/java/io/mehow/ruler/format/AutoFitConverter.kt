package io.mehow.ruler.format

import io.mehow.ruler.ImperialLengthUnit
import io.mehow.ruler.Length
import io.mehow.ruler.LengthUnit
import io.mehow.ruler.SiLengthUnit
import io.mehow.ruler.SiLengthUnit.Centimeter
import io.mehow.ruler.SiLengthUnit.Kilometer
import io.mehow.ruler.SiLengthUnit.Meter
import io.mehow.ruler.SiLengthUnit.Millimeter

/**
 * Converter that applies best fitting [unit][LengthUnit] to a [Length].
 */
public class AutoFitConverter(
  private val siFitting: List<SiLengthUnit> = listOf(Millimeter, Centimeter, Meter, Kilometer),
  private val imperialFitting: List<ImperialLengthUnit> = ImperialLengthUnit.units,
) : LengthConverter {
  override fun convert(length: Length<*>): Length<*> = when (val unit = length.unit) {
    is SiLengthUnit -> length.withUnit(unit).withFittingUnit(siFitting)
    is ImperialLengthUnit -> length.withUnit(unit).withFittingUnit(imperialFitting)
  }

  internal object Factory : LengthConverter.Factory {
    private val converter = AutoFitConverter()

    override fun create(length: Length<*>) = converter
  }
}
