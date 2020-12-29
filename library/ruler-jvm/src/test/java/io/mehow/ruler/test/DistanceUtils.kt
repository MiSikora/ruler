package io.mehow.ruler.test

import io.mehow.ruler.Distance
import io.mehow.ruler.ImperialLengthUnit.Foot
import io.mehow.ruler.ImperialLengthUnit.Inch
import io.mehow.ruler.ImperialLengthUnit.Mile
import io.mehow.ruler.ImperialLengthUnit.Yard
import io.mehow.ruler.LengthUnit
import io.mehow.ruler.SiLengthUnit.Gigameter
import io.mehow.ruler.SiLengthUnit.Kilometer
import io.mehow.ruler.SiLengthUnit.Megameter
import io.mehow.ruler.SiLengthUnit.Meter
import io.mehow.ruler.SiLengthUnit.Micrometer
import io.mehow.ruler.SiLengthUnit.Millimeter
import io.mehow.ruler.SiLengthUnit.Nanometer

internal fun Distance.toLength(unit: LengthUnit<*>) = when (unit) {
  Nanometer -> toLength(Nanometer)
  Micrometer -> toLength(Micrometer)
  Millimeter -> toLength(Millimeter)
  Meter -> toLength(Meter)
  Kilometer -> toLength(Kilometer)
  Megameter -> toLength(Megameter)
  Gigameter -> toLength(Gigameter)
  Inch -> toLength(Inch)
  Foot -> toLength(Foot)
  Yard -> toLength(Yard)
  Mile -> toLength(Mile)
}
