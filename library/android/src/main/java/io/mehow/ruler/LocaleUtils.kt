package io.mehow.ruler

import java.util.Locale

internal val Locale.isImperial get() = country in Ruler.imperialCountryCodes
