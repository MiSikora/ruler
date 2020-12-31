package io.mehow.ruler.test

import io.kotest.core.listeners.TestListener
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.mehow.ruler.Ruler

internal object ResetRulerListener : TestListener {
  private val isUkImperial = Ruler.isUkImperial
  private val useImperialFormatting = Ruler.useImperialFormatter
  private val driver = Ruler.driver

  override suspend fun afterTest(testCase: TestCase, result: TestResult) {
    Ruler.isUkImperial = isUkImperial
    Ruler.useImperialFormatter = useImperialFormatting
    Ruler.driver = driver
    for (factory in Ruler.installedFormatterFactories) {
      Ruler.removeFormatterFactory(factory)
    }
    for (converter in Ruler.installedConverterFactories) {
      Ruler.removeConverterFactory(converter)
    }
  }
}
