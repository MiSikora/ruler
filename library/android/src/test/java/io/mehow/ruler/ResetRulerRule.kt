package io.mehow.ruler

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

internal object ResetRulerRule : TestRule {
  override fun apply(base: Statement, description: Description): Statement {
    return object : Statement() {
      override fun evaluate() {
        val isUkImperial = Ruler.isUkImperial
        val useImperialFormatting = Ruler.useImperialFormatter
        try {
          return base.evaluate()
        } finally {
          Ruler.isUkImperial = isUkImperial
          Ruler.useImperialFormatter = useImperialFormatting
        }
      }
    }
  }
}
