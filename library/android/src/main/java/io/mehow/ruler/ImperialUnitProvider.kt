package io.mehow.ruler

import io.mehow.ruler.ImperialLengthUnit.Foot
import io.mehow.ruler.ImperialLengthUnit.Inch
import io.mehow.ruler.ImperialLengthUnit.Mile
import io.mehow.ruler.ImperialLengthUnit.Yard

object ImperialUnitProvider : UnitResourceProvider<ImperialLengthUnit> {
  override fun resource(unit: ImperialLengthUnit) = when (unit) {
    Inch -> R.string.io_mehow_ruler_inches
    Foot -> R.string.io_mehow_ruler_feet
    Yard -> R.string.io_mehow_ruler_yards
    Mile -> R.string.io_mehow_ruler_miles
  }
}
