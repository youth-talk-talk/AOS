import com.youth.app.setNamespace

plugins {
    id("youth.android.feature")
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrainsCompose)
}

android {
    setNamespace("feature.community")
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(projects.core.base)
    implementation(libs.material)
    implementation(libs.coil3.coil.compose)
    implementation(libs.accompanist.swiperefresh)
}
