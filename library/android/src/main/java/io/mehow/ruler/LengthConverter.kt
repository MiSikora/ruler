package io.mehow.ruler

interface LengthConverter {
  fun convert(length: Length<*>): Length<*>
}
