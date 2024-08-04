import com.youth.app.setNamespace

plugins {
    id("youth.android.library")
    id("youth.android.hilt")
}

android {
    setNamespace("core.dataApi")
}

dependencies {
    implementation(projects.core.model)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Paging3
    implementation(libs.androidx.paging.runtime)
    testImplementation(libs.androidx.paging.common)
}
