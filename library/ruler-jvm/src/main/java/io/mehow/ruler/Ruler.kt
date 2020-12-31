package io.mehow.ruler

import io.mehow.ruler.format.AutoFitConverter
import io.mehow.ruler.format.FormattingDriver
import io.mehow.ruler.format.ImperialFormatter
import io.mehow.ruler.format.LengthConverter
import io.mehow.ruler.format.LengthConverter.Factory
import io.mehow.ruler.format.LengthFormatter
import io.mehow.ruler.format.NoOpFormatter

/**
 * A central point for default [Length] formatting.
 *
 * Formatting behaviour can be controlled by installing custom [converter factories][LengthConverter.Factory]
 * or [formatter factories][LengthFormatter.Factory]. If no factories are installed Ruler uses built-in factories
 * to convert and format input.
 */
public object Ruler : LengthConverter, LengthFormatter {
  /**
   * Default driver for lengths and distances formatting.
   */
  @Volatile public var driver: FormattingDriver = FormattingDriver.Builder().build()

  private val builtInConverterFactories = listOf(AutoFitConverter.Factory)

  private val converterFactories = mutableListOf<Factory>()

  /**
   * All available converter factories including built-in ones.
   */
  public val installedConverterFactories: List<Factory>
    get() = converterFactories + builtInConverterFactories

  /**
   * Adds a converter factory to the conversion chain. Factories are applied in an order they are added preceding
   * built-in factories.
   */
  public fun addConverterFactory(factory: Factory): Unit = synchronized(converterFactories) {
    converterFactories += factory
  }

  /**
   * Removes a converter factory from the conversion chain. Built-in factories cannot be removed.
   */
  public fun removeConverterFactory(factory: Factory): Unit = synchronized(converterFactories) {
    converterFactories -= factory
  }

  override fun convert(length: Length<*>): Length<*> = installedConverterFactories
      .asSequence()
      .mapNotNull { factory -> factory.create(length) }
      .map { converter -> converter.convert(length) }
      .first()

  /**
   * Determines whether built-in [imperial formatting][ImperialFormatter] should be applied when system locale
   * supports imperial units.
   */
  @Volatile public var useImperialFormatter: Boolean = true

  private val builtInFormatterFactories = listOf(ImperialFormatter.Factory, NoOpFormatter.Factory)

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

  override fun format(length: Length<*>, driver: FormattingDriver): String = installedFormatterFactories
      .asSequence()
      .mapNotNull { factory -> factory.create(length, driver.formattingContext) }
      .map { formatter -> formatter.format(length, driver) }
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
