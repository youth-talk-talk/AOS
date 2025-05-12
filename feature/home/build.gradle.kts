import com.youth.app.setNamespace

plugins {
    id("youth.android.feature")
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrainsCompose)
}

android {
    setNamespace("feature.home")
}

dependencies {
    implementation(projects.feature.community)
    implementation(projects.feature.policy)
    implementation(projects.feature.mypage)
    implementation(projects.core.base)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
    implementation(libs.coil3.coil.compose)
    implementation(libs.coil3.coil.network.okttp)
}
