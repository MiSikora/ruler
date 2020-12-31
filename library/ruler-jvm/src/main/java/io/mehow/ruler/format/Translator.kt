package io.mehow.ruler.format

import io.mehow.ruler.LengthUnit
import java.util.Locale

/**
 * Provides data necessary for text localization.
 */
public interface Translator {
  /**
   * Locale that should be used for formatting. It has impact on things like decimal point symbol or digit groupings.
   */
  public val locale: Locale

  /**
   * Returns unit's abbreviation appropriate for the [locale].
   */
  public fun symbol(unit: LengthUnit<*>): String
}
