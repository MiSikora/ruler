plugins {
  id("com.android.library")
  kotlin("android")
}

android {
  resourcePrefix("io_mehow_ruler_")

  testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
  api(project(":library:ruler"))

  implementation(Libs.Kotlin.StdLibJdk7)

  testImplementation(Libs.JUnit)
  testImplementation(Libs.KotlinTest.Assertions)
  testImplementation(Libs.AndroidX.Test.CoreKtx)
  testImplementation(Libs.Robolectric)
}

apply(from = rootProject.file("gradle/gradle-mvn-push.gradle"))
