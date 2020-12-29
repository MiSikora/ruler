package io.mehow.ruler.test

import io.mehow.ruler.LengthUnit

internal val LengthUnit<*>.capacity get() = bounds.endInclusive.toLength(this).measure.toLong()
