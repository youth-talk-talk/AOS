package com.youth.app

import gradle.kotlin.dsl.accessors._2fb5859a04200edaf14b854c40b2e363.implementation
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

internal fun Project.configureComposeAndroid() {

    with(plugins) {
        apply("org.jetbrains.kotlin.plugin.compose")
    }

    val libs = extensions.libs
    androidExtension.apply {
        dependencies {
            val bom = libs.findLibrary("androidx.compose.bom").get()
            add("implementation", platform(bom))
            add("androidTestImplementation", platform(bom))

            add("implementation", libs.findLibrary("androidx.ui").get())
            add("implementation", libs.findLibrary("androidx.activity.compose").get())
            add("implementation", libs.findLibrary("androidx.ui.graphics").get())
            add("implementation", libs.findLibrary("androidx.ui.tooling-preview").get())
            add("implementation", libs.findLibrary("androidx.material3").get())
            add("implementation", libs.findLibrary("androidx.paging.compose").get())
            add("implementation", libs.findLibrary("androidx.lifecycle.runtime.compose").get())

            add("androidTestImplementation", libs.findLibrary("androidx.espresso.core").get())
            add("androidTestImplementation", libs.findLibrary("androidx.test.ext").get())
            add("androidTestImplementation", libs.findLibrary("androidx.ui.test.manifest").get())
            add("androidTestImplementation", libs.findLibrary("androidx.ui.test.junit4").get())

            add("debugImplementation", libs.findLibrary("androidx.ui.tooling").get())
            add("debugImplementation", libs.findLibrary("androidx.compose.ui.testManifest").get())
        }
    }

    extensions.getByType<ComposeCompilerGradlePluginExtension>().apply {
        enableStrongSkippingMode.set(true)
        includeSourceInformation.set(true)
    }

}
