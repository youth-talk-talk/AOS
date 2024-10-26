import com.youth.app.setNamespace

plugins {
//    id("youth.kotlin.library")
    id("youth.android.library")
    id("kotlinx-serialization")
    id("com.google.devtools.ksp")
    kotlin("plugin.serialization")
}

android {
    setNamespace("core.model")
}

dependencies {
    implementation(libs.androidx.annotation)
    implementation(libs.kotlinx.serialization.json)

    // Room
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.room.compiler)
    ksp(libs.androidx.room.room.compiler)
    implementation(libs.androidx.room.paging)
    implementation(libs.androidx.room.ktx)
}
