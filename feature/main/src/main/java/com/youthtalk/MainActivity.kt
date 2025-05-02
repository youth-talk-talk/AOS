package com.youthtalk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import com.core.navigation.navigator.LoginNavigator
import com.youthtalk.designsystem.YongProjectTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var loginNavigator: LoginNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YongProjectTheme {
                Box {
                    Scaffold { innerPadding ->
                        MainScreen(
                            modifier = Modifier.padding(innerPadding),
                            goLogin = {
                                loginNavigator.navigateFrom(
                                    activity = this@MainActivity,
                                    withFinish = true,
                                )
                            },
                            checkPermission = { permission ->
                                ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity, permission)
                            },
                        )
                    }
                    CustomStatusBarColor(color = Color.Transparent)
                }
            }
        }
    }

    @Composable
    fun CustomStatusBarColor(color: Color) {
        val insets = WindowInsets.statusBars.asPaddingValues()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(insets.calculateTopPadding())
                .background(color),
        )
    }
}
