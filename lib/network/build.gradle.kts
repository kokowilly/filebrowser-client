plugins {
  id("java-library")
  alias(libs.plugins.kotlin.jvm)
}

java {
  sourceCompatibility = JavaVersion.VERSION_17
  targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
  implementation(libs.dagger)
  implementation(libs.retrofit.core)
  implementation(libs.retrofit.moshi.converter)
  implementation(libs.moshi.kotlin)
  implementation(libs.moshi.kotlin.codegen)
  implementation(libs.kotlinx.coroutines.core)

  testImplementation(project(":test:common"))
  testImplementation(libs.kotlinx.coroutines.test)
}
