plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
    id("androidx.navigation.safeargs.kotlin")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.10"
    id("tg-plugin")
    id("com.google.devtools.ksp") version "2.0.0-1.0.21"
    id("com.google.dagger.hilt.android")
}

pluginExtension {
    enableSizeCheck.set(true)
    fileSizeLimitInMb.set(50)
}

android {
    namespace = "com.example.playgroundyandexschool"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.playgroundyandexschool"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        manifestPlaceholders["YANDEX_CLIENT_ID"] = "07d3c81536d64b6a877dbe39ae1d562d"
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

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.converter.gson)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.recyclerview.swipedecorator)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.retrofit)
    implementation(libs.converter.scalars)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.authsdk)
    implementation(libs.bundles.room)
    ksp(libs.room.compiler)
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.div)
    implementation(libs.div.core)
    implementation(libs.div.json)
    implementation("com.yandex.div:picasso:30.8.0")
}