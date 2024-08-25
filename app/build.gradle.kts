plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.ksp)
}

android {
  namespace = "id.kokowilly.filebrowser"
  compileSdk = 34

  defaultConfig {
    applicationId = "id.kokowilly.filebrowser"
    minSdk = 26
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  kotlinOptions {
    jvmTarget = "17"
  }
}

dependencies {
  implementation(project(":core:log"))
  implementation(project(":core:navigation"))
  implementation(project(":lib:network"))
  implementation(project(":feature:connection"))
  implementation(project(":feature:browse"))

  implementation(libs.koin.core)
  implementation(libs.koin.android)

  implementation(libs.coil)
  implementation(libs.coil.gif)
  implementation(libs.coil.svg)

  implementation(libs.androidx.multidex)

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.material)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
}
