package io.mehow.ruler.format

import android.content.Context

/**
 * Updates this builder with properties appropriate for Android applications.
 */
public fun FormattingDriver.Builder.withAndroidContext(
  context: Context,
): FormattingDriver.Builder = withTranslator(ContextTranslator(context)).withMeasureFormatter(IcuFormatter)
