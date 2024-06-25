import com.youth.app.setNamespace

plugins {
    id("youth.android.library")
    id("youth.android.compose")
}

android {
    setNamespace("core.designsystem")
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
}
