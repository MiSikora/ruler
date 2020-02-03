package io.mehow.ruler

interface DistanceConverter {
  fun convert(distance: Distance<*>): Distance<*>
}
