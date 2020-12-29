package io.mehow.ruler

/**
 * Algorithm for selecting the best fitting [unit][LengthUnit].
 */
public interface UnitFitter {
  /**
   * Selects a unit from available units that is the best fit for a supplied length.
   */
  public fun <T : LengthUnit<T>> findFit(units: Iterable<T>, length: Length<T>): T?
}
