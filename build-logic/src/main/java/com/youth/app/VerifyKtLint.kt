package com.youth.app

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.version

internal fun Project.configureVerifyKtLint() {
    with(pluginManager) {
        apply("org.jlleitschuh.gradle.ktlint")
    }
}
