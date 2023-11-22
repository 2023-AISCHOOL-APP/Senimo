plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  id("kotlin-kapt")
}

android {
  namespace = "com.example.senimoapplication"
  compileSdk = 34
  viewBinding{
    this.enable = true
  }

  defaultConfig {
    applicationId = "com.example.senimoapplication"
    minSdk = 24
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {

    buildFeatures {
      viewBinding = true
    }

    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
}

dependencies {

  // Glide 라이브러리
  implementation ("com.github.bumptech.glide:glide:4.16.0")
  // Glide를 위한 Kotlin 지원 라이브러리
  kapt ("com.github.bumptech.glide:compiler:4.16.0")

  implementation("androidx.core:core-ktx:1.12.0")
  implementation("androidx.appcompat:appcompat:1.6.1")
  implementation("androidx.activity:activity-compose:1.8.1")
  implementation ("androidx.activity:activity-ktx:1.8.1")
  implementation("com.google.android.material:material:1.10.0")
  implementation("androidx.constraintlayout:constraintlayout:2.1.4")
  implementation ("androidx.viewpager2:viewpager2:1.0.0")
  implementation("androidx.gridlayout:gridlayout:1.0.0")
  implementation ("com.google.code.gson:gson:2.8.9")
  implementation ("com.squareup.retrofit2:retrofit:2.9.0")
  implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
  implementation ("com.squareup.okhttp3:logging-interceptor:4.9.1")
  implementation ("com.github.bumptech.glide:glide:4.16.0")
  implementation("androidx.recyclerview:recyclerview:1.3.2")
  annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0") //Glide의 어노테이션 프로세서 추가
  testImplementation("junit:junit:4.13.2")

  androidTestImplementation("androidx.test.ext:junit:1.1.5")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


}