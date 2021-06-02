package io.mehow.ruler.sample

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.widget.AppCompatSpinner
import io.mehow.ruler.LengthUnit
import androidx.appcompat.R as AppCompatR

@Suppress("VariableDefinition")
internal class UnitViewGroup @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet,
  defStyle: Int = AppCompatR.attr.spinnerStyle,
) : AppCompatSpinner(context, attrs, defStyle) {
  var selectedUnit: LengthUnit<*>? = null

  init {
    adapter = UnitAdapter()
    onItemSelectedListener = object : OnItemSelectedListener {
      override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        selectedUnit = adapter!!.getItem(position)
      }

      override fun onNothingSelected(parent: AdapterView<*>?) {
        selectedUnit = null
      }
    }
  }

  override fun getAdapter() = super.getAdapter() as? UnitAdapter
}
