package io.mehow.ruler.format

import io.mehow.ruler.Length

/**
 * Formatter that delegates all formatting to a [driver][FormattingDriver].
 */
public object NoOpFormatter : LengthFormatter {
  override fun format(length: Length<*>, driver: FormattingDriver): String = driver.format(length)

  internal object Factory : LengthFormatter.Factory {
    override fun create(length: Length<*>, context: FormattingContext) = NoOpFormatter
  }
}
