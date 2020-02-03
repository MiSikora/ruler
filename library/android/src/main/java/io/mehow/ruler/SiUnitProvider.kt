package io.mehow.ruler

import io.mehow.ruler.SiLengthUnit.Gigameter
import io.mehow.ruler.SiLengthUnit.Kilometer
import io.mehow.ruler.SiLengthUnit.Megameter
import io.mehow.ruler.SiLengthUnit.Meter
import io.mehow.ruler.SiLengthUnit.Micrometer
import io.mehow.ruler.SiLengthUnit.Millimeter
import io.mehow.ruler.SiLengthUnit.Nanometer

object SiUnitProvider : UnitResourceProvider<SiLengthUnit> {
  override fun resource(unit: SiLengthUnit) = when (unit) {
    Nanometer -> R.string.io_mehow_ruler_nanometers
    Micrometer -> R.string.io_mehow_ruler_micrometers
    Millimeter -> R.string.io_mehow_ruler_millimeters
    Meter -> R.string.io_mehow_ruler_meters
    Kilometer -> R.string.io_mehow_ruler_kilometers
    Megameter -> R.string.io_mehow_ruler_megameters
    Gigameter -> R.string.io_mehow_ruler_gigameters
  }
}
