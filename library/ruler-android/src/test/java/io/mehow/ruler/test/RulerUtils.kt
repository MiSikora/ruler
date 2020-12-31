package io.mehow.ruler.test

import android.content.Context
import io.mehow.ruler.Ruler
import io.mehow.ruler.format.withAndroidContext

internal fun Ruler.contextualize(context: Context) {
  driver = driver.newBuilder().withAndroidContext(context).build()
}
