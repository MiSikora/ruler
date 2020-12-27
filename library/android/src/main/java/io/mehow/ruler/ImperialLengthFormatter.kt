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

  override fun Length<*>.format(context: Context, separator: String): String? {
    val parts = distance.prepareParts(context, separator)
    return when {
      parts.isEmpty() -> minPartFormatter?.format(0, context, separator)
      else -> parts.joinToString(partSeparator)
    }
  }

  private fun Distance.prepareParts(
    context: Context,
    separator: String,
  ) = formatters.fold(this to emptyList<String>()) { (partDistance, parts), formatter ->
    val unitCount = partDistance.toLength(formatter.unit).measure.toLong()
    if (unitCount == 0L) return@fold partDistance to parts

    val adjustedDistance = partDistance - Distance.of(unitCount, formatter.unit)
    val formattedParts = parts + formatter.format(unitCount, context, separator)
    adjustedDistance to formattedParts
  }.second

  public companion object {
    public val Full: ImperialLengthFormatter = Builder()
        .withMiles()
        .withYards()
        .withFeet()
        .withInches()
        .build()
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
    fun format(unitCount: Long, context: Context, separator: String) = context.getString(resource, unitCount, separator)

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
