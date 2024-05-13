import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.kspPlugins) apply false
    alias(libs.plugins.hiltPlugins) apply false
    id("org.jlleitschuh.gradle.ktlint") version "12.1.1"
    id("io.gitlab.arturbosch.detekt") version "1.23.3"
}

allprojects {
    apply {
        plugin("io.gitlab.arturbosch.detekt")
    }

    afterEvaluate {
        detekt {
            // 설정한 파일의 경로를 입력
            config.setFrom(files("$rootDir/config/detekt-config.yml"))
            // 설정 파일에서 직접 입력한 부분들만, 기본 룰 설정에서 부분적으로 덮어쓸지 결정한다.
            buildUponDefaultConfig = true
            debug = true
            toolVersion = "1.23.3"
            allRules = false
        }
    }

    // Kotlin DSL
    tasks.withType<Detekt>().configureEach {
        jvmTarget = "17"
    }

    val reportMerge by tasks.registering(io.gitlab.arturbosch.detekt.report.ReportMergeTask::class) {
        output.set(rootProject.layout.buildDirectory.file("reports/detekt/detekt_report.xml")) // or "reports/detekt/merge.xml"
    }

    subprojects {

        tasks.withType<Detekt>().configureEach {
            finalizedBy(reportMerge)
            reports {
                sarif.required.set(true)
            }
        }

        reportMerge {
            input.from(tasks.withType<Detekt>().map { it.xmlReportFile }) // or .sarifReportFile
        }
    }

    tasks.withType<DetektCreateBaselineTask>().configureEach {
        jvmTarget = "17"
    }
}
