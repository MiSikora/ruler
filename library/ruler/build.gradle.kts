plugins {
  kotlin("jvm")
}

dependencies {
  implementation(Libs.Kotlin.StdLibJdk7)

  testImplementation(Libs.KotlinTest.Runner)
}

apply(from = rootProject.file("gradle/gradle-mvn-push.gradle"))
