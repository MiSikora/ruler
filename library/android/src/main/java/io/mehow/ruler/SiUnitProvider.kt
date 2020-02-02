package io.mehow.ruler

import io.mehow.ruler.SiDistanceUnit.Gigameter
import io.mehow.ruler.SiDistanceUnit.Kilometer
import io.mehow.ruler.SiDistanceUnit.Megameter
import io.mehow.ruler.SiDistanceUnit.Meter
import io.mehow.ruler.SiDistanceUnit.Micrometer
import io.mehow.ruler.SiDistanceUnit.Millimeter
import io.mehow.ruler.SiDistanceUnit.Nanometer

object SiUnitProvider : DistanceUnitResourceProvider<SiDistanceUnit> {
  override fun resource(unit: SiDistanceUnit) = when (unit) {
    Nanometer -> R.string.io_mehow_ruler_nanometers
    Micrometer -> R.string.io_mehow_ruler_micrometers
    Millimeter -> R.string.io_mehow_ruler_millimeters
    Meter -> R.string.io_mehow_ruler_meters
    Kilometer -> R.string.io_mehow_ruler_kilometers
    Megameter -> R.string.io_mehow_ruler_megameters
    Gigameter -> R.string.io_mehow_ruler_gigameters
  }
}
