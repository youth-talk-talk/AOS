import com.youth.app.configureVerifyDetekt
import io.gitlab.arturbosch.detekt.Detekt
import org.gradle.api.JavaVersion
import org.gradle.kotlin.dsl.withType

configureVerifyDetekt()

// Kotlin DSL
tasks.withType<Detekt>().configureEach {
    jvmTarget = JavaVersion.VERSION_19.majorVersion

    buildUponDefaultConfig = true
    allRules = false
    parallel = true
    config.setFrom("$rootDir/config/detekt-config.yml")

    reports {
        file("$rootDir/build/reports/test/${project.name}/").mkdirs()
        html.required.set(true) // observe findings in your browser with structure and code snippets
        html.outputLocation.set(file("$rootDir/build/reports/detekt/${project.name}.html"))
        xml.required.set(true) // checkstyle like format mainly for integrations like Jenkins
        xml.outputLocation.set(file("$rootDir/build/reports/detekt/${project.name}.xml"))
    }
}
