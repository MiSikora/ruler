package io.mehow.ruler

import android.content.Context

/**
 * Converts one [Length] to another one.
 */
public fun interface LengthConverter {
  /**
   * Maps an input [Length] to a one that should be formatted.
   */
  public fun Length<*>.convert(context: Context): Length<*>

  /**
   * Factory for [LengthConverter] that can be [installed][Ruler.addConverterFactory] in [Ruler].
   */
  public fun interface Factory {
    /**
     * Creates a [LengthConverter] that will be used to process an input. It should return `null` if no converter
     * created by this factory can handle the input.
     */
    public fun create(length: Length<*>, context: Context): LengthConverter?
  }
}
