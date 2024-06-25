plugins {
//    alias(libs.plugins.androidApplication)
//    alias(libs.plugins.jetbrainsKotlinAndroid)
//    alias(libs.plugins.kspPlugins)
//    alias(libs.plugins.hiltPlugins)
//    id("org.jlleitschuh.gradle.ktlint") version "12.1.1"
//    id("io.gitlab.arturbosch.detekt")
    id("youth.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.youth.yongproject.app"

    defaultConfig {
        applicationId = "com.youth.yongproject.app"
        targetSdk = 34
        versionCode = 1
        versionName = "0.0.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(projects.feature.main)
    implementation(projects.feature.login)
    implementation(projects.core.designsystem)
    implementation(platform(libs.firebase.bom))
    //noinspection UseTomlInstead
    implementation("com.google.firebase:firebase-analytics")
}

task("printVersionName") {
    println("${project.android.defaultConfig.versionName}")
}
