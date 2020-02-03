package io.mehow.ruler

internal object BuiltInLengthUnitProvider : UnitResourceProvider<LengthUnit> {
  override fun resource(unit: LengthUnit) = when (unit) {
    is SiLengthUnit -> SiUnitProvider.resource(unit)
    is ImperialLengthUnit -> ImperialUnitProvider.resource(unit)
    else -> error("Unknown unit '$unit'. Use custom unit provider.")
  }
}
