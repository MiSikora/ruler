package io.mehow.ruler.test

import android.content.Context
import io.mehow.ruler.Length
import io.mehow.ruler.LengthFormatter

internal fun LengthFormatter.format(
  length: Length<*>,
  context: Context,
  unitSeparator: String = "",
) = with(this) { length.format(unitSeparator, context) }
