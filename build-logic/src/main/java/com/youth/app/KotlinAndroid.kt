package com.youth.app

import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.support.kotlinCompilerOptions
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureKotlinAndroid() {
    //plugins
    with(pluginManager) {
        apply("org.jetbrains.kotlin.android")
    }
    configureVerifyKtLint()

    fun getApiKey(propertyKey: String): String {
        return gradleLocalProperties(rootDir, providers).getProperty(propertyKey)
    }

    androidExtension.apply {

        compileSdk = 34

        defaultConfig {
            minSdk = 26

            buildConfigField("String", "KAKAO_API_KEY", getApiKey("kakao.api.key"))
            addManifestPlaceholders(mapOf("KAKAO_API_KEY" to getApiKey("kakao.api.key")))
        }

        buildTypes {
            getByName("release") {
                isMinifyEnabled = false
                proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            }
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_19
            targetCompatibility = JavaVersion.VERSION_19
        }
        buildFeatures {
            buildConfig = true
        }
        gradle.startParameter.excludedTaskNames.addAll(listOf(":build-logic:clean"))
        gradle.startParameter.excludedTaskNames.addAll(listOf(":build-logic:compileTestKotlin"))
        gradle.startParameter.excludedTaskNames.addAll(listOf(":build-logic:processTestResources"))
    }

    configureKotlin()

    val libs = extensions.libs

    dependencies {
        add("coreLibraryDesugaring", libs.findLibrary("android.desugarJdkLibs").get())
        add("implementation", libs.findLibrary("gson").get())
        add("implementation", libs.findLibrary("kakao.v2.user").get())
    }

}

internal fun Project.configureKotlin() {
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_19)
            // Treat all Kotlin warnings as errors (disabled by default)
            // Override by setting warningsAsErrors=true in your ~/.gradle/gradle.properties
            val warningsAsErrors: String? by project
            allWarningsAsErrors.set(warningsAsErrors.toBoolean())
            freeCompilerArgs.set(
                freeCompilerArgs.get() + listOf(
                    "-opt-in=kotlin.RequiresOptIn",
                )
            )
        }
    }
}
