import com.youth.app.setNamespace

plugins {
    id("youth.android.library")
}

android {
    setNamespace("core.datastore")
}

dependencies {
    implementation(libs.androidx.datastore.preferences.core)
    implementation(libs.androidx.datastore.preferences)
}
