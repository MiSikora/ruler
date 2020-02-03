/*
 * Copyright (c) 2007-present, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
@file:Suppress("ComplexMethod", "StringLiteralDuplication", "ComplexCondition")

package io.mehow.ruler

import kotlin.Long.Companion.MIN_VALUE

internal fun Long.safeAdd(b: Long): Long {
  val sum = this + b
  if (this xor sum < 0 && this xor b >= 0) {
    throw ArithmeticException("Addition overflows a long: $this + $b")
  }
  return sum
}

internal fun Long.safeSubtract(b: Long): Long {
  val result = this - b
  if (this xor result < 0 && this xor b < 0) {
    throw ArithmeticException("Subtraction overflows a long: $this - $b")
  }
  return result
}

internal fun Long.safeMultiply(b: Int): Long {
  when (b) {
    -1 -> {
      if (this == MIN_VALUE) {
        throw ArithmeticException("Multiplication overflows a long: $this * $b")
      }
      return -this
    }
    0 -> return 0L
    1 -> return this
  }
  val total = this * b
  if (total / b != this) {
    throw ArithmeticException("Multiplication overflows a long: $this * $b")
  }
  return total
}

internal fun Long.safeMultiply(b: Long): Long {
  if (b == 1L) {
    return this
  }
  if (this == 1L) {
    return b
  }
  if (this == 0L || b == 0L) {
    return 0
  }
  val total = this * b
  if (total / b != this || this == MIN_VALUE && b == -1L || b == MIN_VALUE && this == -1L) {
    throw ArithmeticException("Multiplication overflows a long: $this * $b")
  }
  return total
}

internal fun Long.floorDiv(b: Long): Long {
  return if (this >= 0) this / b else (this + 1) / b - 1
}

internal fun Long.floorMod(b: Long): Long {
  return (this % b + b) % b
}
