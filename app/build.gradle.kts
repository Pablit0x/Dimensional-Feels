plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("dagger.hilt.android.plugin")
    kotlin("kapt")
    id ("io.realm.kotlin")
    id ("com.google.gms.google-services")
}

android {
    namespace = "com.ps.dimensional_feels"
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        applicationId = "com.ps.dimensional_feels"
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = ProjectConfig.extensionVersion
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime)
    implementation(libs.activity.compose)
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation(libs.material3.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.junit.ext)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")


    // Compose Navigation
    implementation (libs.navigation.compose)

    // Firebase
    implementation (libs.firebase.auth)
    implementation (libs.firebase.storage)

    // Room components
    implementation (libs.room.runtime)
    kapt (libs.room.compiler)
    implementation (libs.room.ktx)

    // Runtime Compose
    implementation (libs.runtime.compose)

    // Splash API
    implementation (libs.splash.api)

    // Mongo DB Realm
    implementation (libs.coroutines.core)
    implementation (libs.realm.sync)

    // Dagger Hilt
    implementation (libs.hilt.android)
    kapt (libs.hilt.compiler)
    implementation (libs.hilt.navigation.compose)

    // Coil
    implementation (libs.coil)

    // Pager - Accompanist
    implementation (libs.accompanist.pager)

    // Date-Time Picker
    implementation (libs.date.time.picker)

    // CALENDAR
    implementation (libs.date.dialog)

    // CLOCK
    implementation (libs.time.dialog)

    // Message Bar Compose
    implementation (libs.message.bar.compose)

    // One-Tap Compose
    implementation (libs.one.tap.compose)

    // Desugar JDK
    coreLibraryDesugaring (libs.desugar.jdk)

    implementation(project(":core:ui"))
    implementation(project(":core:util"))
    implementation(project(":data:mongo"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:home"))
    implementation(project(":feature:write"))
}