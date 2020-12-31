package io.mehow.ruler.test

import io.mehow.ruler.ImperialLengthUnit.Foot
import io.mehow.ruler.ImperialLengthUnit.Inch
import io.mehow.ruler.ImperialLengthUnit.Mile
import io.mehow.ruler.ImperialLengthUnit.Yard
import io.mehow.ruler.LengthUnit
import io.mehow.ruler.SiLengthUnit.Centimeter
import io.mehow.ruler.SiLengthUnit.Decameter
import io.mehow.ruler.SiLengthUnit.Decimeter
import io.mehow.ruler.SiLengthUnit.Gigameter
import io.mehow.ruler.SiLengthUnit.Hectometer
import io.mehow.ruler.SiLengthUnit.Kilometer
import io.mehow.ruler.SiLengthUnit.Megameter
import io.mehow.ruler.SiLengthUnit.Meter
import io.mehow.ruler.SiLengthUnit.Micrometer
import io.mehow.ruler.SiLengthUnit.Millimeter
import io.mehow.ruler.SiLengthUnit.Nanometer
import io.mehow.ruler.format.Translator
import java.util.Locale

internal class LocaleTranslator(
  override val locale: Locale,
) : Translator {
  override fun symbol(unit: LengthUnit<*>): String = when (unit) {
    Nanometer -> "nm"
    Micrometer -> "µm"
    Millimeter -> "mm"
    Centimeter -> "cm"
    Decimeter -> "dm"
    Meter -> "m"
    Decameter -> "dam"
    Hectometer -> "hm"
    Kilometer -> "km"
    Megameter -> "Mm"
    Gigameter -> "Gm"
    Inch -> "in"
    Foot -> "ft"
    Yard -> "yd"
    Mile -> "mi"
  }
}
