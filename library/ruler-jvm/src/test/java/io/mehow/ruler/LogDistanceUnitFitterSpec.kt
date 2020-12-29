package io.mehow.ruler

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll
import io.mehow.ruler.ImperialLengthUnit.Mile
import io.mehow.ruler.SiLengthUnit.Centimeter
import io.mehow.ruler.SiLengthUnit.Decameter
import io.mehow.ruler.SiLengthUnit.Decimeter
import io.mehow.ruler.SiLengthUnit.Gigameter
import io.mehow.ruler.SiLengthUnit.Hectometer
import io.mehow.ruler.SiLengthUnit.Kilometer
import io.mehow.ruler.SiLengthUnit.Megameter
import io.mehow.ruler.SiLengthUnit.Meter
import io.mehow.ruler.SiLengthUnit.Micrometer
import io.mehow.ruler.SiLengthUnit.Millimeter
import io.mehow.ruler.SiLengthUnit.Nanometer
import io.mehow.ruler.test.LengthGenerator

internal class LogDistanceUnitFitterSpec : DescribeSpec({
  describe("fitter") {
    it("zero length selects smallest available unit") {
      for (unit in SiLengthUnit.units - SiLengthUnit.units.last()) {
        val units = SiLengthUnit.units.filter { it > unit }
        LogDistanceUnitFitter.findFit(units, Distance.Zero.toLength(Meter)) shouldBe units.first()
      }

      for (unit in ImperialLengthUnit.units - ImperialLengthUnit.units.last()) {
        val units = ImperialLengthUnit.units.filter { it > unit }
        LogDistanceUnitFitter.findFit(units, Distance.Zero.toLength(Mile)) shouldBe units.first()
      }
    }

    it("skips lengths when there are no units") {
      for (unit in SiLengthUnit.units) {
        checkAll(LengthGenerator.unitRange(unit)) { length ->
          LogDistanceUnitFitter.findFit(emptyList(), length) shouldBe null
        }
      }

      for (unit in ImperialLengthUnit.units) {
        checkAll(LengthGenerator.unitRange(unit)) { length ->
          LogDistanceUnitFitter.findFit(emptyList(), length) shouldBe null
        }
      }
    }

    it("finds fit in range") {
      for (unit in SiLengthUnit.units) {
        checkAll(LengthGenerator.unitRange(unit)) { length ->
          LogDistanceUnitFitter.findFit(SiLengthUnit.units, length) shouldBe unit
        }
      }

      for (unit in ImperialLengthUnit.units) {
        checkAll(LengthGenerator.unitRange(unit)) { length ->
          LogDistanceUnitFitter.findFit(ImperialLengthUnit.units, length) shouldBe unit
        }
      }
    }

    it("finds fit to the closest range based on logarithmic distance") {
      forAll(
          row(Distance.Epsilon.toLength(Nanometer), listOf(Nanometer), Micrometer),
          row(Length.of(31, Micrometer), listOf(Micrometer), Nanometer),
          row(Length.of(32, Micrometer), listOf(Micrometer), Millimeter),
          row(Length.of(3.1, Millimeter), listOf(Millimeter), Micrometer),
          row(Length.of(3.2, Millimeter), listOf(Millimeter), Centimeter),
          row(Length.of(3.1, Centimeter), listOf(Centimeter), Millimeter),
          row(Length.of(3.2, Centimeter), listOf(Centimeter), Decimeter),
          row(Length.of(3.1, Decimeter), listOf(Decimeter), Centimeter),
          row(Length.of(3.2, Decimeter), listOf(Decimeter), Meter),
          row(Length.of(3.1, Meter), listOf(Meter), Decimeter),
          row(Length.of(3.2, Meter), listOf(Meter), Decameter),
          row(Length.of(3.1, Decameter), listOf(Decameter), Meter),
          row(Length.of(3.2, Decameter), listOf(Decameter), Hectometer),
          row(Length.of(3.1, Hectometer), listOf(Hectometer), Decameter),
          row(Length.of(3.2, Hectometer), listOf(Hectometer), Kilometer),
          row(Length.of(31, Kilometer), listOf(Kilometer), Hectometer),
          row(Length.of(32, Kilometer), listOf(Kilometer), Megameter),
          row(Length.of(31, Megameter), listOf(Megameter), Kilometer),
          row(Length.of(32, Megameter), listOf(Megameter), Gigameter),
          row(Distance.Max.toLength(Gigameter), listOf(Gigameter), Megameter),
      ) { length, excludedUnits: List<SiLengthUnit>, expectedUnit ->
        LogDistanceUnitFitter.findFit(SiLengthUnit.units - excludedUnits, length) shouldBe expectedUnit
        LogDistanceUnitFitter.findFit(SiLengthUnit.units - excludedUnits, -length) shouldBe expectedUnit
      }
    }
  }
})
