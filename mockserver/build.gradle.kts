plugins {
    kotlin("jvm")
    application
}

dependencies {
    implementation(libs.mockwebserver)
}

application {
    mainClass.set("com.example.mybank.mockserver.MockServerLauncherKt")
}

