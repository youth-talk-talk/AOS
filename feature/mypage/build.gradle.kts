import com.youth.app.setNamespace

plugins {
    id("youth.android.feature")
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrainsCompose)
}

android {
    setNamespace("feature.mypage")
}

dependencies {
    implementation(projects.core.base)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.coil3.coil.compose)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
}
