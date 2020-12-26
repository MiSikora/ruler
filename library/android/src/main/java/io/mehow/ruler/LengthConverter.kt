package io.mehow.ruler

import android.content.Context

public fun interface LengthConverter {
  public fun Length<*>.convert(context: Context): Length<*>?
}
