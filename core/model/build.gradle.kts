plugins {
    id("youth.kotlin.library")
    id("kotlinx-serialization")
    kotlin("plugin.serialization")
}

dependencies {
    implementation(libs.androidx.annotation)
    implementation(libs.kotlinx.serialization.json)
}
