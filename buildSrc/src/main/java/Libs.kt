object Libs {
  const val AndroidGradlePlugin = "com.android.tools.build:gradle:4.0.0"

  object Kotlin {
    const val Version = "1.3.72"

    const val GradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$Version"

    const val StdLibJdk7 = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$Version"
  }

  object AndroidX {
    object Test {
      const val Version = "1.2.0"

      const val CoreKtx = "androidx.test:core-ktx:$Version"
    }
  }

  const val JUnit = "junit:junit:4.13"

  const val Robolectric = "org.robolectric:robolectric:4.3.1"

  object Kotest {
    const val Version = "4.0.6"

    const val RunnerJunit5 = "io.kotest:kotest-runner-junit5-jvm:$Version"

    const val Assertions = "io.kotest:kotest-assertions-core-jvm:$Version"

    const val Property = "io.kotest:kotest-property-jvm:$Version"
  }

  const val MavenPublishGradlePlugin = "com.vanniktech:gradle-maven-publish-plugin:0.11.1"

  object Detekt {
    const val Version = "1.9.1"

    const val GradlePluginId = "io.gitlab.arturbosch.detekt"

    const val GradlePlugin = "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:$Version"

    const val Formatting = "io.gitlab.arturbosch.detekt:detekt-formatting:$Version"

    const val Cli = "io.gitlab.arturbosch.detekt:detekt-cli:$Version"
  }

  object GradleVersions {
    const val Version = "0.28.0"

    const val GradlePluginId = "com.github.ben-manes.versions"

    const val GradlePlugin = "com.github.ben-manes:gradle-versions-plugin:$Version"
  }
}
