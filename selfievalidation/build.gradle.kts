plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.mybank.selfievalidation"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 30
    }

    testOptions {
        targetSdk = 36
        lint {
            targetSdk = 36
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

    implementation(libs.bundles.coroutines)
    implementation(libs.bundles.koin)
    implementation(libs.bundles.network)

    ksp(libs.moshi.kotlin.codegen)

    implementation(libs.bundles.camera)

    debugImplementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.bundles.composeDebug)
}

