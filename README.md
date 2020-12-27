# Ruler 📐

![Quality Check CI](https://github.com/MiSikora/Ruler/workflows/Quality%20Check/badge.svg?branch=trunk&event=push)
![Snapshot CI](https://github.com/MiSikora/Ruler/workflows/Snapshot/badge.svg?branch=trunk&event=push)
[<img src="https://img.shields.io/maven-central/v/io.mehow.ruler/ruler.svg?label=latest%20release"/>](https://search.maven.org/search?q=g:io.mehow.ruler)
[<img src="https://img.shields.io/nexus/s/https/oss.sonatype.org/io.mehow.ruler/ruler.svg?label=latest%20snapshot"/>](https://oss.sonatype.org/content/repositories/snapshots/io/mehow/ruler/)
![GitHub License](https://img.shields.io/github/license/MiSikora/Ruler)

Distance measurements library for Kotlin and Android.

Please visit [project website](https://misikora.github.io/Ruler/) for the full documentation and the [changelog](https://misikora.github.io/Ruler/changelog/).

## TLDR

Add Ruler dependency to your project.

```groovy
repositories {
  mavenCentral()
}

dependencies {
  implementation "io.mehow.ruler:ruler-android:0.6.0"
}
```

Enable Java 8 support.

```groovy
android {
  compileOptions {
    sourceCompatibility JavaVersion.VERSION_1_8
    targetCompatibility JavaVersion.VERSION_1_8
  }

  kotlinOptions {
    jvmTarget = "1.8"
  }
}
```

Define a distance and manipulate it.

```kotlin
// Distance is dimensionless.
val distanceFromMeters: Distance = Distance.ofMeters(100)
val distanceFromYards: Distance = Distance.ofYards(50)

// Length has a unit attached to it.
val metersLength: Length<SiLengthUnit.Meter> = distanceFromMeters.toLength(SiLengthUnit.Meter)
val inchesLength: Length<ImperialLengthUnit.Inch> = distanceFromMeters.toLength(ImperialLengthUnit.Inch)

// metersLength and inchesLength represent the same distance but with a different units attached to them.
check(metersLength - inchesLength == Length.ofMeters(0))
```

Print distances and lengths in a human-readable way based on Locale.

```kotlin
fun main(context: Context) {
  val distance = Distance.ofMeters(100)
  val length = distance.toLength(Meter)

  // Assumes en_US Locale on a device.

  // Prints "109yd 1ft 1in".
  val humanReadableDistance: String = distance.format(context)

  // Prints "100.00m".
  val humanReadableLength: String = length.format(context)
}
```

## License

    Copyright 2020 Michał Sikora

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
