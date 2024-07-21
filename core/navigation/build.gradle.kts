import com.youth.app.setNamespace

plugins {
    id("youth.android.library")
    id("youth.android.compose")
    alias(libs.plugins.kotlin.serialization)
}

android {
    setNamespace("core.navigation")
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
}
