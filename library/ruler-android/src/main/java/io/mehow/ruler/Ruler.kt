package io.mehow.ruler

import android.content.Context
import io.mehow.ruler.ImperialLengthUnit.Yard
import io.mehow.ruler.SiLengthUnit.Meter

/**
 * A central point for default [Distance] and [Length] formatting.
 *
 * Formatting behaviour can be controlled by installing custom [converter factories][LengthConverter.Factory]
 * or [formatter factories][LengthFormatter.Factory]. If no factories are installed Ruler uses built-in factories
 * to convert and format input.
 */
public object Ruler : LengthConverter, LengthFormatter {
  private val builtInConverterFactories = listOf(
      LengthConverter.Factory { _, _ -> AutoFitLengthConverter },
  )

  private val converterFactories = mutableListOf<LengthConverter.Factory>()

  /**
   * All available converter factories including built-in ones.
   */
  public val installedConverterFactories: List<LengthConverter.Factory>
    get() = converterFactories + builtInConverterFactories

  /**
   * Adds a converter factory to the conversion chain. Factories are applied in an order they are added preceding
   * built-in factories.
   */
  public fun addConverterFactory(factory: LengthConverter.Factory): Unit = synchronized(converterFactories) {
    converterFactories += factory
  }

  /**
   * Removes a converter factory from the conversion chain. Built-in factories cannot be removed.
   */
  @Deprecated(
      message = "This method will be removed in next major release.",
      replaceWith = ReplaceWith("removeConverterFactory(factory)"),
  )
  public fun removeConverter(factory: LengthConverter.Factory): Unit = synchronized(converterFactories) {
    converterFactories -= factory
  }

  /**
   * Removes a converter factory from the conversion chain. Built-in factories cannot be removed.
   */
  public fun removeConverterFactory(factory: LengthConverter.Factory): Unit = synchronized(converterFactories) {
    converterFactories -= factory
  }

  override fun Length<*>.convert(context: Context): Length<*> = installedConverterFactories
      .asSequence()
      .mapNotNull { factory -> factory.create(this, context) }
      .map { converter -> with(converter) { convert(context) } }
      .first()

  /**
   * Determines whether built-in [imperial formatting][ImperialLengthFormatter] should be applied when system locale
   * supports imperial units.
   */
  @Volatile public var useImperialFormatter: Boolean = true

  private val builtInFormatterFactories = listOf(
      ImperialLengthFormatter.AllPartsFactory(partSeparator = " ") { useImperialFormatter },
      LengthFormatter.Factory { _, _, _ -> AutoLengthFormatter },
  )

  private val formatterFactories = mutableListOf<LengthFormatter.Factory>()

  /**
   * All available formatter factories including built-in ones.
   */
  public val installedFormatterFactories: List<LengthFormatter.Factory>
    get() = formatterFactories + builtInFormatterFactories

  /**
   * Adds a formatter factory to the formatting chain. Factories are applied in an order they are added preceding
   * built-in factories.
   */
  public fun addFormatterFactory(factory: LengthFormatter.Factory): Unit = synchronized(formatterFactories) {
    formatterFactories += factory
  }

  /**
   * Removes a formatter factory from the formatting chain. Built-in factories cannot be removed.
   */
  public fun removeFormatterFactory(factory: LengthFormatter.Factory): Unit = synchronized(formatterFactories) {
    formatterFactories -= factory
  }

  override fun Length<*>.format(unitSeparator: String, context: Context): String = installedFormatterFactories
      .asSequence()
      .mapNotNull { factory -> factory.create(this, unitSeparator, context) }
      .map { formatter -> with(formatter) { format(unitSeparator, context) } }
      .first()

  private val builtInImperialCountryCodes = setOf("US", "LR", "MM")

  private val imperialCountryCodes = mutableSetOf<String>()

  /**
   * All available country codes that should use imperial system.
   */
  public val installedImperialCountryCodes: Set<String>
    get() = builtInImperialCountryCodes + imperialCountryCodes

  /**
   * Determines whether United Kingdom should use imperial system.
   */
  public var isUkImperial: Boolean
    get() = ukCountryCode in imperialCountryCodes
    set(add) {
      val func = if (add) imperialCountryCodes::add else imperialCountryCodes::remove
      synchronized(imperialCountryCodes) { func(ukCountryCode) }
    }

  private const val ukCountryCode = "GB"
}

/**
 * Formats this distance to a human readable form. Uses [Yard] or [Meter] as a base unit depending on a system locale.
 *
 * @param context Context that will be used for determining the [unit system][LengthUnit].
 * @param unitSeparator Separator that should be used between a numeric value and a unit.
 * @param converter Conversion rules that should be applied before formatting this distance.
 * @param formatter Formatting rules that should be applied to format this distance.
 */
public fun Distance.format(
  context: Context,
  unitSeparator: String = "",
  converter: LengthConverter? = Ruler,
  formatter: LengthFormatter = Ruler,
): String = when {
  context.useImperialUnits -> toLength(Yard)
  else -> toLength(Meter)
}.format(context, unitSeparator, converter, formatter)

/**
 * Formats this length to a human readable form.
 *
 * @param context Context that will be used for determining the [unit system][LengthUnit].
 * @param unitSeparator Separator that should be used between a numeric value and a unit.
 * @param converter Conversion rules that should be applied before formatting this length.
 * @param formatter Formatting rules that should be applied to format this length.
 */
public fun Length<*>.format(
  context: Context,
  unitSeparator: String = "",
  converter: LengthConverter? = Ruler,
  formatter: LengthFormatter = Ruler,
): String {
  val length = converter?.run { convert(context) } ?: this
  return with(formatter) { length.format(unitSeparator, context) }
}
