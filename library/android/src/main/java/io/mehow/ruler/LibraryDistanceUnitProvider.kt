package io.mehow.ruler

object LibraryDistanceUnitProvider : DistanceUnitResourceProvider<DistanceUnit> {
  override fun resource(unit: DistanceUnit) = when (unit) {
    is SiDistanceUnit -> SiUnitProvider.resource(unit)
    is ImperialDistanceUnit -> ImperialUnitProvider.resource(unit)
    else -> error("Unknown unit '$unit'. Use custom unit provider.")
  }
}
