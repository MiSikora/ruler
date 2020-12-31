package io.mehow.ruler

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.arbitrary.filter
import io.kotest.property.checkAll
import io.mehow.ruler.test.DistanceGenerator
import io.mehow.ruler.test.LengthGenerator

internal class InRangeUnitFitterSpec : DescribeSpec({
  describe("fitter") {
    it("skips distances that are not in any range") {
      for (unit in SiLengthUnit.units) {
        checkAll(LengthGenerator.unitRange(unit)) { length ->
          InRageUnitFitter.findFit(SiLengthUnit.units - unit, length) shouldBe null
        }
      }

      for (unit in ImperialLengthUnit.units) {
        checkAll(LengthGenerator.unitRange(unit)) { length ->
          InRageUnitFitter.findFit(ImperialLengthUnit.units - unit, length) shouldBe null
        }
      }
    }

    it("finds fit in range") {
      for (unit in SiLengthUnit.units) {
        checkAll(LengthGenerator.unitRange(unit)) { length ->
          InRageUnitFitter.findFit(SiLengthUnit.units, length) shouldBe unit
        }
      }

      for (unit in ImperialLengthUnit.units) {
        checkAll(LengthGenerator.unitRange(unit)) { length ->
          InRageUnitFitter.findFit(ImperialLengthUnit.units, length) shouldBe unit
        }
      }
    }
  }
})
