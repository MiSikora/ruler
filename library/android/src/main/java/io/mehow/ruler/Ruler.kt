package io.mehow.ruler

import android.content.Context
import io.mehow.ruler.ImperialLengthUnit.Yard
import io.mehow.ruler.SiLengthUnit.Meter

public object Ruler : LengthConverter, LengthFormatter {
  private val builtInConverterFactories = listOf(
      LengthConverter.Factory { _, _ -> AutoFitLengthConverter },
  )

  private val converterFactories = mutableListOf<LengthConverter.Factory>()

  public val installedConverterFactories: List<LengthConverter.Factory>
    get() = builtInConverterFactories + converterFactories

  public fun addConverterFactory(factory: LengthConverter.Factory): Unit = synchronized(converterFactories) {
    converterFactories += factory
  }

  public fun removeConverter(factory: LengthConverter.Factory): Unit = synchronized(converterFactories) {
    converterFactories -= factory
  }

  override fun Length<*>.convert(context: Context): Length<*>? = installedConverterFactories
      .asSequence()
      .mapNotNull { factory -> factory.create(this, context) }
      .map { converter -> with(converter) { convert(context) } }
      .firstOrNull()

  @Volatile public var useImperialFormatter: Boolean = true

  private val builtInFormatterFactories = listOf(
      ImperialLengthFormatter.Factory(partSeparator = " ") { useImperialFormatter },
      LengthFormatter.Factory { _, _, _ -> AutoLengthFormatter },
  )

  private val formatterFactories = mutableListOf<LengthFormatter.Factory>()

  public val installedFormatterFactories: List<LengthFormatter.Factory>
    get() = builtInFormatterFactories + formatterFactories

  public fun addFormatterFactory(factory: LengthFormatter.Factory): Unit = synchronized(formatterFactories) {
    formatterFactories += factory
  }

  public fun removeFormatterFactory(factory: LengthFormatter.Factory): Unit = synchronized(formatterFactories) {
    formatterFactories -= factory
  }

  override fun Length<*>.format(unitSeparator: String, context: Context): String? = installedFormatterFactories
      .asSequence()
      .mapNotNull { factory -> factory.create(this, unitSeparator, context) }
      .map { formatter -> with(formatter) { format(unitSeparator, context) } }
      .firstOrNull()

  private val builtInImperialCountryCodes = setOf("US", "LR", "MM")

  private val imperialCountryCodes = mutableSetOf<String>()

  public val installedImperialCountryCodes: Set<String>
    get() = builtInImperialCountryCodes + imperialCountryCodes

  public var isUkImperial: Boolean
    get() = ukCountryCode in imperialCountryCodes
    set(add) {
      val func = if (add) imperialCountryCodes::add else imperialCountryCodes::remove
      synchronized(imperialCountryCodes) { func(ukCountryCode) }
    }

  private const val ukCountryCode = "GB"
}

public fun Distance.format(
  context: Context,
  unitSeparator: String = "",
  converter: LengthConverter? = Ruler,
  formatter: LengthFormatter = Ruler,
): String = when {
  context.useImperialUnits -> toLength(Yard)
  else -> toLength(Meter)
}.format(context, unitSeparator, converter, formatter)

public fun Length<*>.format(
  context: Context,
  unitSeparator: String = "",
  converter: LengthConverter? = Ruler,
  formatter: LengthFormatter = Ruler,
): String {
  val length = converter?.run { convert(context) } ?: this
  val text = with(formatter) { length.format(unitSeparator, context) }
  return checkNotNull(text) { "Failed to format length: $length" }
}
