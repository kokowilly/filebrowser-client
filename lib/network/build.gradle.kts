plugins {
  id("java-library")
  alias(libs.plugins.jetbrains.kotlin.jvm)
}

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
  implementation(libs.retrofit.core)
  implementation(libs.retrofit.moshi.converter)
  implementation(libs.moshi.kotlin)
  implementation(libs.moshi.kotlin.codegen)
  implementation(libs.kotlinx.coroutines.core)
}