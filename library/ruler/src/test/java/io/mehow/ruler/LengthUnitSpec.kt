package io.mehow.ruler

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll
import io.mehow.ruler.test.DistanceGenerator

internal class LengthUnitSpec : DescribeSpec({
  describe("unit") {
    it("contains distances within its bounds") {
      for (unit in LengthUnit.units) {
        checkAll(DistanceGenerator.create(unit)) { distance ->
          unit.contains(distance) shouldBe true
          unit.contains(-distance) shouldBe true
        }
      }
    }

    it("does not contain distances outside of its bounds") {
      for (unit in LengthUnit.units) {
        val otherUnits = when (unit) {
          is SiLengthUnit -> SiLengthUnit.units - unit
          is ImperialLengthUnit -> ImperialLengthUnit.units - unit
        }
        for (otherUnit in otherUnits) {
          checkAll(DistanceGenerator.create(otherUnit)) { distance ->
            unit.contains(distance) shouldBe false
            unit.contains(-distance) shouldBe false
          }
        }
      }
    }
  }
})
