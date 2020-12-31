package io.mehow.ruler.format

import io.mehow.ruler.Length

/**
 * Converts one [Length] to another one.
 */
public fun interface LengthConverter {
  /**
   * Maps an input [Length] to a one that should be formatted.
   */
  public fun convert(length: Length<*>): Length<*>

  /**
   * Factory for [LengthConverter] that can be [installed][io.mehow.ruler.Ruler.addConverterFactory]
   * in [io.mehow.ruler.Ruler].
   */
  public fun interface Factory {
    /**
     * Creates a [LengthConverter] that will be used to process an input. It should return `null` if no converter
     * created by this factory can handle the input.
     */
    public fun create(length: Length<*>): LengthConverter?
  }
}
