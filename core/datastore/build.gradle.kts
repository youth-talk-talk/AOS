import com.youth.app.setNamespace

plugins {
    id("youth.android.library")
    id("kotlinx-serialization")
    kotlin("plugin.serialization")
}

android {
    setNamespace("core.datastore")
}

dependencies {
    implementation(projects.core.model)
    implementation(libs.androidx.datastore.preferences.core)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.serialization.json)
}
