package io.mehow.ruler

import android.content.Context

public interface LengthFormatter {
  public fun Length<*>.format(context: Context, separator: String): String?
}
