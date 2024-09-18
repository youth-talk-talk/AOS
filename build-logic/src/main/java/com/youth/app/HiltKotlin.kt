package com.youth.app

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureHiltKotlin() {
    with(pluginManager) {
        apply("com.google.devtools.ksp")
    }

    val libs = extensions.libs
    dependencies {
        add("implementation", libs.findLibrary("hilt.core").get())
        add("implementation", libs.findLibrary("androidx.hilt.work").get())
        add("ksp", libs.findLibrary("hilt.compiler").get())
        add("ksp", libs.findLibrary("androidx.hilt.compiler").get())
    }
}

internal class HiltKotlinPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            configureHiltKotlin()
        }
    }
}
