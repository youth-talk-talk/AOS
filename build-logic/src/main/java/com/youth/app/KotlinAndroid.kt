package com.youth.app

import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.provideDelegate
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

        compileSdk = 35

        defaultConfig {
            minSdk = 26

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

            buildConfigField("String", "KAKAO_API_KEY", getApiKey("kakao.api.key"))
            addManifestPlaceholders(mapOf("KAKAO_API_KEY" to getApiKey("kakao.api.xml.key")))
        }

        if(System.getenv("CI") != "true") {
            signingConfigs {
                create("release") {
                    storeFile = file(getApiKey("KEYSTORE_FILE"))
                    storePassword = getApiKey("KEYSTORE_PASSWORD")
                    keyAlias = getApiKey("KEY_ALIAS")
                    keyPassword = getApiKey("KEY_PASSWORD")
                }
            }
        }


        buildTypes {
            getByName("release") {
                isMinifyEnabled = false
                buildConfigField("String", "SERVER_KEY", getApiKey("server_key"))
                proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            }

            getByName("debug") {
                buildConfigField("String", "SERVER_KEY", getApiKey("dev_server_key"))
                proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            }
        }

        flavorDimensions.add("versions")
        productFlavors {
            create("dev") {
                buildConfigField("String", "BASE_URL", getApiKey("dev_server_key"))
            }
            create("prod") {
                buildConfigField("String", "BASE_URL", getApiKey("server_key"))
            }
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }

        buildFeatures {
            buildConfig = true
        }
        lint {
            abortOnError = false
            checkReleaseBuilds = false
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
        add("implementation", libs.findLibrary("timber").get())


        add("testImplementation",libs.findLibrary("junit").get())
        add("androidTestImplementation",libs.findLibrary("androidx-junit").get())
        add("androidTestImplementation",libs.findLibrary("androidx-espresso-core").get())
    }

}

internal fun Project.configureKotlin() {
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
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
