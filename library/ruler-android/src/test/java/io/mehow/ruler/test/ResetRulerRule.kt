package io.mehow.ruler.test

import io.mehow.ruler.Ruler
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

internal object ResetRulerRule : TestRule {
  override fun apply(base: Statement, description: Description) = object : Statement() {
    override fun evaluate() {
      val isUkImperial = Ruler.isUkImperial
      val useImperialFormatting = Ruler.useImperialFormatter
      try {
        return base.evaluate()
      } finally {
        Ruler.isUkImperial = isUkImperial
        Ruler.useImperialFormatter = useImperialFormatting
        for (factory in Ruler.installedFormatterFactories) {
          Ruler.removeFormatterFactory(factory)
        }
        for (converter in Ruler.installedConverterFactories) {
          Ruler.removeConverterFactory(converter)
        }
      }
    }
  }
}
