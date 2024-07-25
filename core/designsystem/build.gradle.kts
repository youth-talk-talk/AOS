import com.youth.app.setNamespace

plugins {
    id("youth.android.library")
    id("youth.android.compose")
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrainsCompose)
}

android {
    setNamespace("core.designsystem")
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(projects.core.model)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
}
