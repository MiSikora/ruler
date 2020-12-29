package io.mehow.ruler

import kotlin.math.absoluteValue
import kotlin.math.log10

/**
 * Selects a unit based on logarithmic difference between [distance][Length.distance]
 * and one of [unit bounds][LengthUnit.bounds].
 */
public object LogDistanceUnitFitter : UnitFitter {
  override fun <T : LengthUnit<T>> findFit(
    units: Iterable<T>,
    length: Length<T>,
  ): T? = length.distance.abs().let { distance ->
    units.associateWith { LogDelta(distance, it) }.minByOrNull { (_, delta) -> delta }?.key
  }

  private class LogDelta<T : LengthUnit<T>>(reference: Distance, unit: T) : Comparable<LogDelta<T>> {
    private val referenceDistance = reference.logMeters

    private val delta = setOf(unit.bounds.start.logMeters, unit.bounds.endInclusive.logMeters)
        .associateWith { (referenceDistance - it).absoluteValue }
        .minOf { (_, delta) -> delta }

    override fun compareTo(other: LogDelta<T>) = delta.compareTo(other.delta)

    private val Distance.logMeters
      get() = when {
        // Don't go to a negative infinity.
        this == Distance.Zero -> Double.MIN_VALUE
        // Drop decimal part to account for rounding errors when converting from a BigDecimal to a Double directly.
        this >= roundingErrorDistance -> meters.toLong()
        else -> meters
      }.toDouble().let(::log10)

    private companion object {
      // This value is established by very precise calculations™.
      val roundingErrorDistance = Distance.ofNanometers(999_999_999_500_000_000)
    }
  }
}
