package io.mehow.ruler.sample

import android.app.Activity
import android.os.Bundle
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import io.mehow.ruler.Distance
import io.mehow.ruler.ImperialLengthUnit.Foot
import io.mehow.ruler.ImperialLengthUnit.Inch
import io.mehow.ruler.ImperialLengthUnit.Mile
import io.mehow.ruler.ImperialLengthUnit.Yard
import io.mehow.ruler.LengthUnit
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
import io.mehow.ruler.format

internal class SampleActivity : Activity() {
  override fun onCreate(inState: Bundle?) {
    super.onCreate(inState)
    setContentView(R.layout.io_mehow_ruler_sample_main)

    val lengthValue = findViewById<TextInputEditText>(R.id.lengthValue)
    val lengthUnit = findViewById<UnitViewGroup>(R.id.lengthUnit)
    val printLength = findViewById<MaterialButton>(R.id.printLength)
    val length = findViewById<MaterialTextView>(R.id.length)

    printLength.setOnClickListener {
      val value = lengthValue.text?.toString()?.toDoubleOrNull() ?: return@setOnClickListener
      val unit = lengthUnit.selectedUnit ?: return@setOnClickListener
      val distance = runCatching { Distance.of(value, unit) }.getOrNull() ?: return@setOnClickListener
      length.text = distance.toLength(unit).format(this, converter = { this })
    }
  }

  private fun Distance.toLength(unit: LengthUnit<*>) = when (unit) {
    Nanometer -> toLength(Nanometer)
    Micrometer -> toLength(Micrometer)
    Millimeter -> toLength(Millimeter)
    Centimeter -> toLength(Centimeter)
    Decimeter -> toLength(Decimeter)
    Meter -> toLength(Meter)
    Decameter -> toLength(Decameter)
    Hectometer -> toLength(Hectometer)
    Kilometer -> toLength(Kilometer)
    Megameter -> toLength(Megameter)
    Gigameter -> toLength(Gigameter)
    Inch -> toLength(Inch)
    Foot -> toLength(Foot)
    Yard -> toLength(Yard)
    Mile -> toLength(Mile)
  }
}
