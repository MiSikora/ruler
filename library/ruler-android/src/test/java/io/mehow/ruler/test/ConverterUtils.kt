package io.mehow.ruler.test

import android.content.Context
import io.mehow.ruler.Length
import io.mehow.ruler.LengthConverter

internal fun LengthConverter.convert(
  length: Length<*>,
  context: Context,
) = with(this) { length.convert(context) }
