package io.mehow.ruler

import android.content.Context
import android.content.res.Resources
import android.text.TextUtils
import android.view.View
import java.util.Locale

class ImperialDistanceFormatter internal constructor(
  private val formatters: Set<PartFormatter>,
  private val partsSeparator: String
) {
  fun format(
    distance: Distance,
    context: Context
  ): String {
    val reverseOrder = context.preferredLocale.isRtl
    return formattedParts(distance, context, reverseOrder).joinToString(partsSeparator)
  }

  fun format(
    length: Length<ImperialLengthUnit>,
    context: Context
  ): String {
    return format(length.distance, context)
  }

  private fun formattedParts(
    distance: Distance,
    context: Context,
    reverseOrder: Boolean
  ): List<String> {
    val highestHierarchy = formatters.map(PartFormatter::hierarchy).min() ?: -1L
    val parts = formatters.mapNotNull { formatter ->
      formatter.format(distance, context.resources, formatter.hierarchy == highestHierarchy)
    }
    return if (reverseOrder) parts.reversed() else parts
  }

  companion object {
    @JvmField val Basic = Builder()
        .withMiles()
        .withYards()
        .withFeet()
        .build()

    @JvmField val Full = Builder()
        .withMiles(printZeros = true)
        .withYards(printZeros = true)
        .withFeet(printZeros = true)
        .withInches(printZeros = true)
        .build()
  }

  private val Locale.isRtl: Boolean
    get() {
      return TextUtils.getLayoutDirectionFromLocale(this) == View.LAYOUT_DIRECTION_RTL
    }

  class Builder private constructor(private val formatters: Set<PartFormatter>) {
    constructor() : this(emptySet())

    @JvmOverloads fun withMiles(
      valueSeparator: String = "",
      printZeros: Boolean = false
    ): Builder {
      return append(PartFormatter.Miles(valueSeparator, printZeros))
    }

    @JvmOverloads fun withYards(
      valueSeparator: String = "",
      printZeros: Boolean = false
    ): Builder {
      return append(PartFormatter.Yards(valueSeparator, printZeros))
    }

    @JvmOverloads fun withFeet(
      valueSeparator: String = "",
      printZeros: Boolean = false
    ): Builder {
      return append(PartFormatter.Feet(valueSeparator, printZeros))
    }

    @JvmOverloads fun withInches(
      valueSeparator: String = "",
      printZeros: Boolean = false
    ): Builder {
      return append(PartFormatter.Inches(valueSeparator, printZeros))
    }

    fun build(partsSeparator: String = " "): ImperialDistanceFormatter {
      return ImperialDistanceFormatter(formatters, partsSeparator)
    }

    private fun append(formatter: PartFormatter): Builder {
      return Builder(formatters + formatter)
    }
  }
}

internal sealed class PartFormatter {
  abstract val separator: String
  abstract val printZeros: Boolean
  abstract val hierarchy: Int
  abstract val resource: Int

  fun format(distance: Distance, resources: Resources, isBasePart: Boolean): String? {
    val part = distance.extractPart() + if (isBasePart) distance.extractBasePart() else 0L
    return if (printZeros || part != 0L) resources.getString(resource, part, separator) else null
  }

  protected abstract fun Distance.extractPart(): Long

  protected abstract fun Distance.extractBasePart(): Long

  abstract override fun equals(other: Any?): Boolean

  abstract override fun hashCode(): Int

  class Miles(
    override val separator: String,
    override val printZeros: Boolean
  ) : PartFormatter() {
    override val hierarchy = 0
    override val resource = R.string.io_mehow_ruler_miles_part

    override fun Distance.extractPart() = milesPart

    override fun Distance.extractBasePart() = 0L

    override fun equals(other: Any?) = other is Miles

    override fun hashCode() = 1
  }

  class Yards(
    override val separator: String,
    override val printZeros: Boolean
  ) : PartFormatter() {
    override val hierarchy = 1
    override val resource = R.string.io_mehow_ruler_yards_part

    override fun Distance.extractPart() = yardsPart

    override fun Distance.extractBasePart() = totalMiles.toLong() * 1_760

    override fun equals(other: Any?) = other is Yards

    override fun hashCode() = 2
  }

  class Feet(
    override val separator: String,
    override val printZeros: Boolean
  ) : PartFormatter() {
    override val hierarchy = 2
    override val resource = R.string.io_mehow_ruler_feet_part

    override fun Distance.extractPart() = feetPart

    override fun Distance.extractBasePart() = totalYards.toLong() * 3

    override fun equals(other: Any?) = other is Feet

    override fun hashCode() = 3
  }

  class Inches(
    override val separator: String,
    override val printZeros: Boolean
  ) : PartFormatter() {
    override val hierarchy = 3
    override val resource = R.string.io_mehow_ruler_inches_part

    override fun Distance.extractPart() = inchesPart

    override fun Distance.extractBasePart() = totalFeet.toLong() * 12

    override fun equals(other: Any?) = other is Inches

    override fun hashCode() = 4
  }
}

internal val Distance.totalMiles get() = exactTotalMeters / 1_609.344.toBigDecimal()

internal val Distance.milesPart get() = totalMiles.toLong()

internal val Distance.totalYards get() = exactTotalMeters / 0.9_144.toBigDecimal()

internal val Distance.yardsPart get() = totalYards.toLong() % 1_760

internal val Distance.totalFeet get() = exactTotalMeters / 0.3_048.toBigDecimal()

internal val Distance.feetPart get() = totalFeet.toLong() % 3

internal val Distance.totalInches get() = exactTotalMeters / 0.0_254.toBigDecimal()

internal val Distance.inchesPart get() = totalInches.toLong() % 12
