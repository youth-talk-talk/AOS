import com.youth.app.configureHiltAndroid
import com.youth.app.libs
import gradle.kotlin.dsl.accessors._2fb5859a04200edaf14b854c40b2e363.android
import gradle.kotlin.dsl.accessors._2fb5859a04200edaf14b854c40b2e363.androidTestImplementation
import gradle.kotlin.dsl.accessors._2fb5859a04200edaf14b854c40b2e363.implementation

plugins {
    id("youth.android.library")
    id("youth.android.compose")
}

android {
    packaging {
        resources {
            excludes += "/META-INF/**"
        }
    }
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
}

configureHiltAndroid()

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:domain"))
    implementation(project(":core:navigation"))
    implementation(project(":core:exception"))

    val libs = project.extensions.libs
    implementation(libs.findLibrary("hilt.navigation.compose").get())
    implementation(libs.findLibrary("androidx.navigation.compose").get())
    androidTestImplementation(libs.findLibrary("androidx.compose.navigation.test").get())

    implementation(libs.findLibrary("androidx.lifecycle.viewModelCompose").get())
    implementation(libs.findLibrary("androidx.lifecycle.runtime.ktx").get())
}
