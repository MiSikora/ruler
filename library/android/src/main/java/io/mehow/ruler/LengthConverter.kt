package io.mehow.ruler

import android.content.Context

/**
 * Converts one [Length] to another one. Useful if you need to process an input before it is displayed to a user.
 * Can be automatically applied during data formatting by [installing][Ruler.addConverter] it into the [Ruler].
 */
public fun interface LengthConverter {
  /**
   * Maps an input [Length] to a one that should be formatted. Returns `null` if an input handling
   * should be delegated to a different converter.
   */
  public fun Length<*>.convert(context: Context): Length<*>?
}
