package io.mehow.ruler.format

/**
 * Properties that can be used to format a [length][io.mehow.ruler.Length].
 */
public class FormattingContext internal constructor(
  private val builder: Builder,
) {
  /**
   * Amount of digits that should be shown after a decimal point of a formatted measure.
   */
  public val fractionalPrecision: Int = builder.fractionalPrecision

  /**
   * A separator that should be used between length's numeric value and a unit.
   */
  public val unitSeparator: String = builder.unitSeparator

  /**
   * Returns a builder whose build invocation returns a context with the same configuration as this one.
   */
  public fun newBuilder(): Builder = builder

  /**
   * [FormattingContext] builder.
   */
  public class Builder private constructor(
    internal val fractionalPrecision: Int,
    internal val unitSeparator: String,
  ) {
    public constructor() : this(
        fractionalPrecision = 2,
        unitSeparator = "",
    )

    /**
     * Sets [precision][FormattingContext.fractionalPrecision] that should be used for formatting.
     */
    public fun withPrecision(precision: Int): Builder = Builder(
        fractionalPrecision = precision,
        unitSeparator = unitSeparator,
    )

    /**
     * Sets [separator][FormattingContext.unitSeparator] that should be used for formatting.
     */
    public fun withSeparator(separator: String): Builder = Builder(
        fractionalPrecision = fractionalPrecision,
        unitSeparator = separator,
    )

    /**
     * Creates a new context with properties defined in this builder.
     */
    public fun build(): FormattingContext = FormattingContext(this)
  }

  public companion object {
    /**
     * Creates a context with a specified [precision][FormattingContext.fractionalPrecision].
     */
    public fun withPrecision(precision: Int): FormattingContext = Builder().withPrecision(precision).build()

    /**
     * Creates a context with a specified [separator][FormattingContext.unitSeparator].
     */
    public fun withSeparator(separator: String): FormattingContext = Builder().withSeparator(separator).build()
  }
}
