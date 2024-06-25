import com.youth.app.configureCoroutineAndroid
import com.youth.app.configureHiltAndroid
import com.youth.app.configureKotlinAndroid

plugins {
    id("com.android.library")
    id("youth.verify.detekt")
}

configureKotlinAndroid()
configureCoroutineAndroid()
configureHiltAndroid()
