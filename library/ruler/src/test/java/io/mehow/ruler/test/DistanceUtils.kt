package io.mehow.ruler.test

import io.mehow.ruler.Distance
import io.mehow.ruler.ImperialLengthUnit.Foot
import io.mehow.ruler.ImperialLengthUnit.Inch
import io.mehow.ruler.ImperialLengthUnit.Mile
import io.mehow.ruler.ImperialLengthUnit.Yard
import io.mehow.ruler.Length
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

internal fun Distance.toLength(unit: LengthUnit<*>) = when (unit) {
  Nanometer -> toLength(Nanometer)
  Micrometer -> toLength(Micrometer)
  Millimeter -> toLength(Millimeter)
  Centimeter -> toLength(Centimeter)
  Decimeter -> toLength(Decimeter)
  Meter -> toLength(Meter)
  Decameter -> toLength(Decameter)
  Hectometer -> toLength(Hectometer)
  Kilometer -> toLength(Kilometer)
  Megameter -> toLength(Megameter)
  Gigameter -> toLength(Gigameter)
  Inch -> toLength(Inch)
  Foot -> toLength(Foot)
  Yard -> toLength(Yard)
  Mile -> toLength(Mile)
}

internal fun Length.Companion.of(
  value: Long,
  unit: LengthUnit<*>,
) = Distance.of(value, unit).toLength(unit)

internal fun Length.Companion.of(
  value: Double,
  unit: LengthUnit<*>,
) = Distance.of(value, unit).toLength(unit)
