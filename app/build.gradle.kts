plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

//val retrofitBaseUrl: String = project.findProperty("mockBaseUrl") as? String ?: "http://localhost:8080/"
val retrofitBaseUrl: String = project.findProperty("mockBaseUrl") as? String ?: "http://192.168.0.40:82/"

android {
    namespace = "com.example.mybank"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.mybank"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "BASE_URL", "\"$retrofitBaseUrl\"")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
}

dependencies {
    implementation(libs.bundles.core)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.bundles.composeIntegration)
    implementation(project(":selfievalidation"))

    implementation(libs.bundles.coroutines)
    implementation(libs.bundles.koin)

    implementation(libs.bundles.network)
    ksp(libs.moshi.kotlin.codegen)

    implementation(libs.bundles.room)
    ksp(libs.androidx.room.compiler)

    implementation(libs.bundles.camera)
    implementation(libs.bundles.security)

    testImplementation(libs.bundles.testing)
    testImplementation("org.robolectric:robolectric:4.9.2")       // se usar Robolectric em JVM
    testImplementation("androidx.compose.ui:ui-test-junit4:1.6.0") // Compose UI teste em JVM
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.6.0") // necessário para Compose + Robolectric em debug

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.bundles.androidTest)
    debugImplementation(libs.bundles.composeDebug)
}