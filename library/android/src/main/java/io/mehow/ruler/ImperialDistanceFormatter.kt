package io.mehow.ruler

import android.content.Context
import android.text.TextUtils
import android.view.View
import java.math.BigInteger
import java.util.Locale

public class ImperialDistanceFormatter internal constructor(
  private val formatters: Set<PartFormatter>,
  private val partsSeparator: String,
) {
  public fun format(distance: Distance, context: Context): String = formatParts(
      distance,
      context,
      reverseOrder = context.preferredLocale.isRtl
  )

  public fun format(length: Length<ImperialLengthUnit>, context: Context): String = format(length.distance, context)

  private fun formatParts(distance: Distance, context: Context, reverseOrder: Boolean): String {
    val lowestHierarchy = formatters.map(PartFormatter::hierarchy).maxOrNull()!!
    val highestHierarchy = formatters.map(PartFormatter::hierarchy).minOrNull()!!

    val lengthMap = formatters.associateWith { formatter ->
      val isBasePart = formatter.hierarchy == highestHierarchy
      return@associateWith formatter.part(distance, isBasePart)
    }
    val totalLength = lengthMap.values.fold(BigInteger.ZERO) { acc, length -> acc + length }

    val formattedParts = formatters.mapNotNull { formatter ->
      val part = lengthMap.getValue(formatter)
      return@mapNotNull formatter.printData(context, part, totalLength, lowestHierarchy)
    }

    val localizedParts = if (reverseOrder) formattedParts.reversed() else formattedParts
    return localizedParts.joinToString(partsSeparator)
  }

  private fun PartFormatter.printData(
    context: Context,
    length: BigInteger,
    totalLength: BigInteger,
    lowestAvailableHierarchy: Int,
  ) = when {
    canPrintData(length) -> format(context, length.toLong())
    shouldPrintData(totalLength, lowestAvailableHierarchy) -> format(context, 0L)
    else -> null
  }

  private fun PartFormatter.shouldPrintData(
    totalLength: BigInteger,
    lowestHierarchy: Int,
  ) = totalLength == BigInteger.ZERO && hierarchy == lowestHierarchy

  private fun PartFormatter.canPrintData(part: BigInteger): Boolean = printZeros || part != BigInteger.ZERO

  public companion object {
    @JvmField public val Basic: ImperialDistanceFormatter = Builder()
        .withMiles()
        .withYards()
        .withFeet()
        .build()

    @JvmField public val Full: ImperialDistanceFormatter = Builder()
        .withMiles(printZeros = true)
        .withYards(printZeros = true)
        .withFeet(printZeros = true)
        .withInches(printZeros = true)
        .build()
  }

  private val Locale.isRtl: Boolean
    get() = TextUtils.getLayoutDirectionFromLocale(this) == View.LAYOUT_DIRECTION_RTL

  public class Builder private constructor(
    private val formatters: Set<PartFormatter>,
  ) {
    public constructor() : this(emptySet())

    @JvmOverloads public fun withMiles(
      valueSeparator: String = "",
      printZeros: Boolean = false,
    ): Builder = append(PartFormatter.Miles(valueSeparator, printZeros))

    @JvmOverloads public fun withYards(
      valueSeparator: String = "",
      printZeros: Boolean = false,
    ): Builder = append(PartFormatter.Yards(valueSeparator, printZeros))

    @JvmOverloads public fun withFeet(
      valueSeparator: String = "",
      printZeros: Boolean = false,
    ): Builder = append(PartFormatter.Feet(valueSeparator, printZeros))

    @JvmOverloads public fun withInches(
      valueSeparator: String = "",
      printZeros: Boolean = false,
    ): Builder = append(PartFormatter.Inches(valueSeparator, printZeros))

    public fun build(partsSeparator: String = " "): ImperialDistanceFormatter = ImperialDistanceFormatter(
        formatters,
        partsSeparator
    )

    private fun append(formatter: PartFormatter): Builder = Builder(formatters + formatter)
  }
}

internal sealed class PartFormatter {
  abstract val separator: String
  abstract val printZeros: Boolean
  abstract val hierarchy: Int
  abstract val resource: Int

  fun format(context: Context, distance: Long) = context.getString(resource, distance, separator)

  fun part(distance: Distance, isBasePart: Boolean) = when {
    isBasePart -> distance.extractExtraPart()
    else -> BigInteger.ZERO
  } + distance.extractPart()

  protected abstract fun Distance.extractPart(): BigInteger

  protected abstract fun Distance.extractExtraPart(): BigInteger

  abstract override fun equals(other: Any?): Boolean

  abstract override fun hashCode(): Int

  class Miles(
    override val separator: String,
    override val printZeros: Boolean,
  ) : PartFormatter() {
    override val hierarchy = 0

    override val resource = R.string.io_mehow_ruler_miles_part

    override fun Distance.extractPart() = milesPart

    override fun Distance.extractExtraPart() = BigInteger.ZERO

    override fun equals(other: Any?) = other is Miles

    override fun hashCode() = 1
  }

  class Yards(
    override val separator: String,
    override val printZeros: Boolean,
  ) : PartFormatter() {
    override val hierarchy = 1

    override val resource = R.string.io_mehow_ruler_yards_part

    override fun Distance.extractPart() = yardsPart

    override fun Distance.extractExtraPart() = totalMiles * 1_760.toBigInteger()

    override fun equals(other: Any?) = other is Yards

    override fun hashCode() = 2
  }

  class Feet(
    override val separator: String,
    override val printZeros: Boolean,
  ) : PartFormatter() {
    override val hierarchy = 2

    override val resource = R.string.io_mehow_ruler_feet_part

    override fun Distance.extractPart() = feetPart

    override fun Distance.extractExtraPart() = totalYards * 3.toBigInteger()

    override fun equals(other: Any?) = other is Feet

    override fun hashCode() = 3
  }

  class Inches(
    override val separator: String,
    override val printZeros: Boolean,
  ) : PartFormatter() {
    override val hierarchy = 3

    override val resource = R.string.io_mehow_ruler_inches_part

    override fun Distance.extractPart() = inchesPart

    override fun Distance.extractExtraPart() = totalFeet * 12.toBigInteger()

    override fun equals(other: Any?) = other is Inches

    override fun hashCode() = 4
  }
}

internal val Distance.totalMiles: BigInteger
  get() = (meters / 1_609.344.toBigDecimal()).toBigInteger()

internal val Distance.milesPart get() = totalMiles

internal val Distance.totalYards: BigInteger
  get() = (meters / 0.914_4.toBigDecimal()).toBigInteger()

internal val Distance.yardsPart get() = totalYards % 1_760.toBigInteger()

internal val Distance.totalFeet: BigInteger
  get() = (meters / 0.304_8.toBigDecimal()).toBigInteger()

internal val Distance.feetPart get() = totalFeet % 3.toBigInteger()

internal val Distance.totalInches: BigInteger
  get() = (meters / 0.025_4.toBigDecimal()).toBigInteger()

internal val Distance.inchesPart get() = totalInches % 12.toBigInteger()
