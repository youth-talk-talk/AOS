import com.youth.app.setNamespace

plugins {
    id("youth.android.library")
}

android {
    setNamespace("core.domain")
}

dependencies {

    implementation(projects.core.data)
    implementation(projects.core.dataApi)
    implementation(projects.core.model)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
