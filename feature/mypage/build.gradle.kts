import com.youth.app.setNamespace

plugins {
    id("youth.android.feature")
    id("youth.android.compose")
}

android {
    setNamespace("feature.mypage")
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
