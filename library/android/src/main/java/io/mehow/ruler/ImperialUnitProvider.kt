package io.mehow.ruler

import io.mehow.ruler.ImperialDistanceUnit.Foot
import io.mehow.ruler.ImperialDistanceUnit.Inch
import io.mehow.ruler.ImperialDistanceUnit.Mile
import io.mehow.ruler.ImperialDistanceUnit.Yard

object ImperialUnitProvider : DistanceUnitResourceProvider<ImperialDistanceUnit> {
  override fun resource(unit: ImperialDistanceUnit) = when (unit) {
    Inch -> R.string.io_mehow_ruler_inches
    Foot -> R.string.io_mehow_ruler_feet
    Yard -> R.string.io_mehow_ruler_yards
    Mile -> R.string.io_mehow_ruler_miles
  }
}
