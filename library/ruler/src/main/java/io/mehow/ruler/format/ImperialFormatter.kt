package io.mehow.ruler.format

import io.mehow.ruler.Distance
import io.mehow.ruler.ImperialLengthUnit
import io.mehow.ruler.ImperialLengthUnit.Foot
import io.mehow.ruler.ImperialLengthUnit.Inch
import io.mehow.ruler.ImperialLengthUnit.Mile
import io.mehow.ruler.ImperialLengthUnit.Yard
import io.mehow.ruler.Length
import io.mehow.ruler.Ruler

/**
 * Formatter that applies imperial system friendly rules to a [Length]. For example when length is 3.5 feet it will
 * be displayed as "3ft 6in" instead of "3.5ft".
 *
 * It does not use [precision][FormattingContext.fractionalPrecision] during formatting. Instead, it always uses
 * precision of 0.
 *
 * @see ImperialFormatter.Builder
 */
public class ImperialFormatter internal constructor(
  builder: Builder,
) : LengthFormatter {
  private val formatters = builder.formatters.sortedByDescending(UnitFormatter::formattingUnit)
  private val partSeparator = builder.partSeparator
  private val fallbackFormatter = builder.fallbackFormatter

  override fun format(length: Length<*>, driver: FormattingDriver): String = run {
    val noFractionContext = driver.formattingContext.newBuilder().withPrecision(0).build()
    val noFractionDriver = driver.newBuilder().withFormattingContext(noFractionContext).build()

    val parts = length.formatUnitParts(noFractionDriver)
    when {
      parts.isEmpty() -> fallbackFormatter.format(length, noFractionDriver)
      else -> parts.joinToString(partSeparator)
    }
  }

  private fun Length<*>.formatUnitParts(
    driver: FormattingDriver,
  ) = formatters.fold(distance to emptyList<String>()) { (partDistance, parts), formatter ->
    when {
      partDistance.hasAtLeastOne(formatter.formattingUnit) -> {
        val unitLength = partDistance.toLength(formatter.formattingUnit).roundDown()
        val adjustedDistance = partDistance.abs() - unitLength.abs()
        val part = formatter.format(unitLength, driver)
        adjustedDistance to parts + part
      }
      else -> partDistance to parts
    }
  }.second

  private fun Distance.hasAtLeastOne(unit: ImperialLengthUnit) = this != Distance.Zero && this in unit

  public companion object {
    public val Full: ImperialFormatter = Builder()
        .withMiles()
        .withYards()
        .withFeet()
        .withInches()
        .build()
  }

  /**
   * [ImperialFormatter] builder. Created formatter uses only units that were set during builder construction.
   * Units are always ordered by their capacity from highest to lowest.
   */
  public class Builder private constructor(
    internal val formatters: Set<UnitFormatter>,
    private val fallbackUnit: ImperialLengthUnit,
    internal val partSeparator: String,
  ) {
    internal val fallbackFormatter = formatters.lastOrNull() ?: UnitFormatter(fallbackUnit)

    public constructor() : this(
        formatters = emptySet(),
        fallbackUnit = Yard,
        partSeparator = " ",
    )

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
    public fun build(): ImperialFormatter = ImperialFormatter(this)

    private fun withUnit(unit: ImperialLengthUnit) = Builder(
        formatters + UnitFormatter(unit),
        fallbackUnit,
        partSeparator,
    )
  }

  internal class UnitFormatter(val formattingUnit: ImperialLengthUnit) : LengthFormatter {
    override fun format(length: Length<*>, driver: FormattingDriver) = driver.format(length.withUnit(formattingUnit))

    override fun equals(other: Any?) = other is UnitFormatter && formattingUnit == other.formattingUnit

    override fun hashCode() = formattingUnit.hashCode()
  }

  internal object Factory : LengthFormatter.Factory {
    override fun create(length: Length<*>, context: FormattingContext) = Full.takeIf {
      length.unit is ImperialLengthUnit && Ruler.useImperialFormatter
    }
  }
}
