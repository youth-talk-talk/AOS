import com.youth.app.configureCoroutineAndroid
import com.youth.app.configureHiltAndroid
import com.youth.app.configureKotlin
import com.youth.app.configureKotlinAndroid
import org.gradle.kotlin.dsl.kotlin

plugins {
    kotlin("jvm")
    id("youth.verify.detekt")
}

configureKotlin()
