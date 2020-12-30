package io.mehow.ruler

import android.content.Context
import io.mehow.ruler.ImperialLengthUnit.Foot
import io.mehow.ruler.ImperialLengthUnit.Inch
import io.mehow.ruler.ImperialLengthUnit.Mile
import io.mehow.ruler.ImperialLengthUnit.Yard

/**
 * Formatter that applies imperial system friendly rules to a [Length]. For example when length is 3.5 feet it will
 * be displayed as "3ft 6in" instead of "3.5ft".
 *
 * @see ImperialLengthFormatter.Builder
 */
public class ImperialLengthFormatter internal constructor(
  builder: Builder,
) : LengthFormatter {
  private val formatters = builder.formatters.sortedByDescending(UnitFormatter::unit)
  private val partSeparator = builder.partSeparator
  private val fallbackFormatter = builder.fallbackFormatter

  override fun Length<*>.format(unitSeparator: String, context: Context): String {
    val parts = distance.abs().formatUnitParts(context, unitSeparator)
    val noSignText = when {
      parts.isEmpty() -> {
        val fallbackUnitCount = distance.abs().toLength(fallbackFormatter.unit).measure.toLong()
        fallbackFormatter.format(fallbackUnitCount, context, unitSeparator)
      }
      else -> parts.joinToString(partSeparator)
    }
    return when {
      distance < Distance.Zero -> context.getString(R.string.io_mehow_ruler_imperial_negative_wrapper, noSignText)
      else -> noSignText
    }
  }

  private fun Distance.formatUnitParts(
    context: Context,
    unitSeparator: String,
  ) = formatters.fold(this to emptyList<String>()) { (partDistance, parts), formatter ->
    when {
      partDistance.hasAtLeastOne(formatter.unit) -> {
        val unitCount = partDistance.toLength(formatter.unit).measure.toLong()
        val adjustedDistance = partDistance - Distance.of(unitCount, formatter.unit)
        val part = formatter.format(unitCount, context, unitSeparator)
        adjustedDistance to parts + part
      }
      else -> partDistance to parts
    }
  }.second

  private fun Distance.hasAtLeastOne(unit: ImperialLengthUnit) = this >= Distance.of(1, unit)

  public companion object {
    public val Full: ImperialLengthFormatter = Builder()
        .withMiles()
        .withYards()
        .withFeet()
        .withInches()
        .build()
  }

  /**
   * Factory for [ImperialLengthFormatter] that can conditionally create a formatter with all unit parts.
   *
   * @param partSeparator Separator that should be used between unit parts.
   * @param isEnabled Enables or disables formatter creation.
   */
  public class AllPartsFactory(
    partSeparator: String,
    private val isEnabled: () -> Boolean,
  ) : LengthFormatter.Factory {
    private val formatter = Builder()
        .withMiles()
        .withYards()
        .withFeet()
        .withInches()
        .withPartSeparator(partSeparator)
        .build()

    override fun create(
      length: Length<*>,
      unitSeparator: String,
      context: Context,
    ): LengthFormatter? = formatter.takeIf { length.unit is ImperialLengthUnit && isEnabled() }
  }

  /**
   * [ImperialLengthFormatter] builder. Created formatter uses only units that were set during builder construction.
   * Units are always ordered by their capacity from highest to lowest.
   */
  public class Builder private constructor(
    internal val formatters: Set<UnitFormatter>,
    private val fallbackUnit: ImperialLengthUnit = Yard,
    internal val partSeparator: String = " ",
  ) {
    internal val fallbackFormatter = formatters.lastOrNull() ?: UnitFormatter(fallbackUnit)

    public constructor() : this(emptySet())

    /**
     * Adds miles unit part to the builder.
     */
    public fun withMiles(): Builder = withUnit(Mile)

    /**
     * Adds yards unit part to the builder.
     */
    public fun withYards(): Builder = withUnit(Yard)

    /**
     * Adds feet unit part to the builder.
     */
    public fun withFeet(): Builder = withUnit(Foot)

    /**
     * Adds inches unit part to the builder.
     */
    public fun withInches(): Builder = withUnit(Inch)

    /**
     * Sets part separator between different units.
     */
    public fun withPartSeparator(separator: String): Builder = Builder(formatters, fallbackUnit, separator)

    /**
     * Sets the default unit that should be used for formatting if no unit part was added.
     */
    public fun withFallbackUnit(unit: ImperialLengthUnit): Builder = Builder(formatters, unit, partSeparator)

    /**
     * Creates new formatter with properties defined in this builder.
     */
    public fun build(): ImperialLengthFormatter = ImperialLengthFormatter(this)

    private fun withUnit(unit: ImperialLengthUnit) = Builder(
        formatters + UnitFormatter(unit),
        fallbackUnit,
        partSeparator,
    )
  }

  internal class UnitFormatter(val unit: ImperialLengthUnit) {
    fun format(
      unitCount: Long,
      context: Context,
      unitSeparator: String,
    ) = context.getString(
        R.string.io_mehow_ruler_distance_pattern,
        unitCount.format(context.preferredLocale, precision = 0),
        unitSeparator,
        context.getString(unit.resource),
    )

    override fun equals(other: Any?) = other is UnitFormatter && unit == other.unit

    override fun hashCode() = unit.hashCode()
  }
}
