plugins {
  id("java-library")
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlin.ksp)
}

java {
  sourceCompatibility = JavaVersion.VERSION_17
  targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
  implementation(project(":core:common"))
  implementation(libs.dagger)
  ksp(libs.dagger.compiler)
  implementation(libs.retrofit.core)
  implementation(libs.retrofit.moshi.converter)
  implementation(libs.retrofit.scalar.converter)
  implementation(libs.okhttp.client)
  implementation(libs.okhttp.logging)
  implementation(libs.moshi.kotlin)
  implementation(libs.moshi.kotlin.codegen)
  implementation(libs.kotlinx.coroutines.core)

  testImplementation(project(":test:common"))
  testImplementation(libs.kotlinx.coroutines.test)
}
