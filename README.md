# Ruler

[![Build Status](https://app.bitrise.io/app/07d245b19bfdcfe5/status.svg?token=eLa51er1EPU3_rvwpL99Kw)](https://app.bitrise.io/app/07d245b19bfdcfe5)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.mehow.ruler/ruler/badge.svg)](https://search.maven.org/search?q=g:io.mehow.ruler)
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0)

Distance measurements library for Java and Android.

## Integration

To integrate it with your app you need to add a dependency to you project.

Ruler requires Java 8 bytecode. To enable Java 8 desugaring configure it in your Gradle script.

```groovy
android {
  compileOptions {
    sourceCompatibility JavaVersion.VERSION_1_8
    targetCompatibility JavaVersion.VERSION_1_8
  }
  // For Kotlin projects
  kotlinOptions {
    jvmTarget = "1.8"
  }
}

```groovy
debugImplementation "io.mehow.ruler:ruler:0.4.2"
```

If you use Android you can use Android artifact.

```groovy
debugImplementation "io.mehow.ruler:android:0.4.2"
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
