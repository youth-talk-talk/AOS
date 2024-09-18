plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.verify.detektPlugin)
    compileOnly(libs.compose.compiler.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidHilt") {
            id = "youth.android.hilt"
            implementationClass = "com.youth.app.HiltAndroidPlugin"
        }
        register("kotlinHilt") {
            id = "youth.kotlin.hilt"
            implementationClass = "com.youth.app.HiltKotlinPlugin"
        }
    }
}

tasks.register(":clean", Delete::class) {
    delete = setOf(rootProject.layout.buildDirectory)
}

//task("clean", Delete::class) {
//    delete = setOf(rootProject.layout.buildDirectory)
//}
