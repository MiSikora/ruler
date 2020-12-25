package io.mehow.ruler

import android.content.Context

public interface LengthConverter {
  public fun Length<*>.convert(context: Context): Length<*>?
}
