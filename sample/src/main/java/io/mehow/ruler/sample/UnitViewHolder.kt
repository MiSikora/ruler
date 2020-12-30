package io.mehow.ruler.sample

import com.google.android.material.textview.MaterialTextView
import io.mehow.ruler.LengthUnit

internal class UnitViewHolder(val item: MaterialTextView) {
  fun bind(feature: LengthUnit<*>) {
    item.text = feature.javaClass.simpleName
  }
}
