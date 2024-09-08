import com.youth.app.setNamespace

plugins {
    id("youth.android.feature")
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrainsCompose)
}

android {
    setNamespace("feature.main")
}

dependencies {
    implementation(projects.feature.home)
    implementation(projects.feature.login)
    implementation(projects.feature.mypage)
    implementation(projects.feature.community)
    implementation(projects.feature.policydetail)
    implementation(projects.feature.specpolicy)
    implementation(projects.feature.search)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(projects.core.domain)
}
