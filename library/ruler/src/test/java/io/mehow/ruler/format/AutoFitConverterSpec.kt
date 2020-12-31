package io.mehow.ruler.format

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.collections.shouldNotBeIn
import io.kotest.property.checkAll
import io.mehow.ruler.ImperialLengthUnit
import io.mehow.ruler.ImperialLengthUnit.Inch
import io.mehow.ruler.ImperialLengthUnit.Mile
import io.mehow.ruler.SiLengthUnit
import io.mehow.ruler.SiLengthUnit.Centimeter
import io.mehow.ruler.SiLengthUnit.Decameter
import io.mehow.ruler.SiLengthUnit.Decimeter
import io.mehow.ruler.SiLengthUnit.Kilometer
import io.mehow.ruler.SiLengthUnit.Megameter
import io.mehow.ruler.SiLengthUnit.Meter
import io.mehow.ruler.SiLengthUnit.Millimeter
import io.mehow.ruler.SiLengthUnit.Nanometer
import io.mehow.ruler.test.LengthGenerator

internal class AutoFitConverterSpec : DescribeSpec({
  describe("converter") {
    context("with default construction") {
      val converter = AutoFitConverter()

      it("fits SI lengths only to mm, cm, m or km") {
        checkAll(LengthGenerator.si()) { length ->
          converter.convert(length).unit shouldBeIn listOf(Millimeter, Centimeter, Meter, Kilometer)
        }
      }

      it("fits imperial lengths to all available imperial units") {
        checkAll(LengthGenerator.imperial()) { length ->
          converter.convert(length).unit shouldBeIn ImperialLengthUnit.units
        }
      }
    }

    it("fits SI lengths to specified set of units") {
      val units = listOf(Nanometer, Decimeter, Decameter, Megameter)
      val converter = AutoFitConverter(siFitting = units)
      checkAll(LengthGenerator.si()) { length ->
        converter.convert(length).unit shouldBeIn units
      }
    }

    it("fits imperial lengths to specified set of units") {
      val units = listOf(Inch, Mile)
      val converter = AutoFitConverter(imperialFitting = units)
      checkAll(LengthGenerator.imperial()) { length ->
        converter.convert(length).unit shouldBeIn units
      }
    }

    it("does not use SI fitting to imperial lengths") {
      val converter = AutoFitConverter(SiLengthUnit.units, emptyList())
      checkAll(LengthGenerator.imperial()) { length ->
        converter.convert(length).unit shouldNotBeIn SiLengthUnit.units
      }
    }

    it("does not use imperial fitting to SI lengths") {
      val converter = AutoFitConverter(emptyList(), ImperialLengthUnit.units)
      checkAll(LengthGenerator.si()) { length ->
        converter.convert(length).unit shouldNotBeIn ImperialLengthUnit.units
      }
    }
  }
})
