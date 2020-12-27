package io.mehow.ruler

import android.content.Context
import io.mehow.ruler.ImperialLengthUnit.Foot
import io.mehow.ruler.ImperialLengthUnit.Inch
import io.mehow.ruler.ImperialLengthUnit.Mile
import io.mehow.ruler.ImperialLengthUnit.Yard

public class ImperialLengthFormatter internal constructor(
  builder: Builder,
) : LengthFormatter {
  private val formatters = builder.formatters.sortedByDescending(UnitFormatter::unit)
  private val partSeparator = builder.partSeparator
  private val minPartFormatter = formatters.lastOrNull()

  override fun Length<*>.format(unitSeparator: String, context: Context): String? {
    val parts = distance.abs().formatUnitParts(context, unitSeparator)
    val noSignText = when {
      parts.isEmpty() -> minPartFormatter?.format(0, context, unitSeparator)
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

  public class Factory(
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

  public class Builder private constructor(
    internal val formatters: Set<UnitFormatter>,
    internal val partSeparator: String = " ",
  ) {
    public constructor() : this(emptySet())

    public fun withMiles(): Builder = withUnit(Mile)

    public fun withYards(): Builder = withUnit(Yard)

    public fun withFeet(): Builder = withUnit(Foot)

    public fun withInches(): Builder = withUnit(Inch)

    public fun withPartSeparator(separator: String): Builder = Builder(formatters, separator)

    public fun build(): ImperialLengthFormatter = ImperialLengthFormatter(this)

    private fun withUnit(unit: ImperialLengthUnit) = Builder(formatters + UnitFormatter(unit), partSeparator)
  }

  internal class UnitFormatter(val unit: ImperialLengthUnit) {
    fun format(
      unitCount: Long,
      context: Context,
      unitSeparator: String,
    ) = context.getString(resource, unitCount, unitSeparator)

    override fun equals(other: Any?) = other is UnitFormatter && unit == other.unit

    override fun hashCode() = unit.hashCode()

    private val resource = when (unit) {
      Inch -> R.string.io_mehow_ruler_inches_part
      Foot -> R.string.io_mehow_ruler_feet_part
      Yard -> R.string.io_mehow_ruler_yards_part
      Mile -> R.string.io_mehow_ruler_miles_part
    }
  }
}
