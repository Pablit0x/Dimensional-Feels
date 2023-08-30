plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id ("io.realm.kotlin")
    id ("dagger.hilt.android.plugin")
    kotlin("kapt")
}

android {
    namespace = "com.ps.write"
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        minSdk = ProjectConfig.minSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = ProjectConfig.extensionVersion
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

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.junit.ext)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.realm.sync)
    implementation(libs.material3.compose)
    implementation(libs.activity.compose)
    implementation(libs.navigation.compose)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.auth)

    implementation (libs.accompanist.pager)
    implementation (libs.coil)

    // CALENDAR
    implementation (libs.date.dialog)

    // CLOCK
    implementation (libs.time.dialog)



    implementation(project(":core:util"))
    implementation(project(":data:mongo"))
    implementation(project(":core:ui"))
}