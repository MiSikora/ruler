package io.mehow.ruler.test

import io.mehow.ruler.Ruler
import java.util.Locale

internal fun Ruler.localize(locale: Locale) {
  driver = driver.newBuilder().withTranslator(LocaleTranslator(locale)).build()
}
