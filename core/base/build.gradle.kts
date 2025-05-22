import com.youth.app.setNamespace

plugins {
    id("youth.android.library")
    id("kotlinx-serialization")
    id("com.google.devtools.ksp")
    kotlin("plugin.serialization")
}

android {
    setNamespace("core.base")
}

dependencies {
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
}
