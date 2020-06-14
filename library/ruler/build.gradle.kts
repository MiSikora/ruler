plugins {
  kotlin("jvm")
}

dependencies {
  implementation(Libs.Kotlin.StdLibJdk7)

  testImplementation(Libs.Kotest.RunnerJunit5)
  testImplementation(Libs.Kotest.Assertions)
  testImplementation(Libs.Kotest.Property)
}

apply(from = rootProject.file("gradle/gradle-mvn-push.gradle"))
