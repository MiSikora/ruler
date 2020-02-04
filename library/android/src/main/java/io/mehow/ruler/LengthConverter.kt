package io.mehow.ruler

import android.content.Context

interface LengthConverter {
  fun Length<*>.convert(context: Context): Length<*>?
}
