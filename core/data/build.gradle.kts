import com.youth.app.setNamespace

plugins {
    id("youth.android.library")
    id("youth.android.hilt")
    id("kotlinx-serialization")
    kotlin("plugin.serialization")
}

android {
    setNamespace("core.data")
    defaultConfig {
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += "room.schemaLocation" to "$projectDir/schemas"
            }
        }
    }
    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
}

dependencies {
    implementation(projects.core.model)
    implementation(projects.core.dataApi)
    implementation(projects.core.exception)
    implementation(projects.core.datastore)

    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp3.okhttp)
    implementation(libs.logging.interceptor)
    implementation("com.squareup.okhttp3:okhttp-sse:4.12.0")
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.kotlinx.serialization.json)

    // Paging3
    implementation(libs.androidx.paging.runtime)
    testImplementation(libs.androidx.paging.common)

    // Room
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.room.compiler)
    ksp(libs.androidx.room.room.compiler)
    implementation(libs.androidx.room.paging)
    implementation(libs.androidx.room.ktx)

    testImplementation(libs.okhttp3.mockwebserver)
    testImplementation(libs.mockito.kotlin)
}
