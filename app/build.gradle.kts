import com.youth.app.setNamespace

plugins {
    id("youth.android.application")
    id("com.google.gms.google-services")
}

android {
    setNamespace("yongproject")

    defaultConfig {
        applicationId = "com.youth.yongproject.app"
        targetSdk = 35
        versionCode = 20002
        versionName = "2.0.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
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
