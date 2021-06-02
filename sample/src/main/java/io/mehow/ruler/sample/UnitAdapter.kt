package io.mehow.ruler.sample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.google.android.material.textview.MaterialTextView
import io.mehow.ruler.LengthUnit
import io.mehow.ruler.sample.R.layout

internal class UnitAdapter : BaseAdapter() {
  private val units = LengthUnit.units

  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View = run {
    val inflater = LayoutInflater.from(parent.context)
    val viewHolder = if (convertView == null) {
      val view = inflater.inflate(layout.io_mehow_ruler_unit_spinner_item, parent, false)
      UnitViewHolder(view as MaterialTextView).apply { view.tag = this }
    } else {
      convertView.tag as UnitViewHolder
    }
    viewHolder.bind(getItem(position))
    viewHolder.item
  }

  override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View = run {
    val inflater = LayoutInflater.from(parent.context)
    val viewHolder = if (convertView == null) {
      val view = inflater.inflate(layout.io_mehow_ruler_unit_drop_down_item, parent, false)
      UnitViewHolder(view as MaterialTextView).apply { view.tag = this }
    } else {
      convertView.tag as UnitViewHolder
    }
    viewHolder.bind(getItem(position))
    viewHolder.item
  }

  override fun getCount() = units.size

  override fun getItem(position: Int) = units[position]

  override fun getItemId(position: Int) = position.toLong()
}
