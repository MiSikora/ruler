package io.mehow.ruler

import android.content.Context

interface LengthFormatter {
  fun Length<*>.format(context: Context, separator: String): String?
}
