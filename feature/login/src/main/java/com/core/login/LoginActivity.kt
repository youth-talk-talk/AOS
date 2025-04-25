package com.core.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.core.model.login.LoginUiEffect
import com.core.navigation.navigator.MainNavigator
import com.core.screen.LoginNavHostScreen
import com.youthtalk.designsystem.YongProjectTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var splashScreen: SplashScreen

    @Inject
    lateinit var mainNavigator: MainNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { true }
        setObserver()

        setContent {
            LaunchedEffect(viewModel.uiEffect) {
                viewModel.uiEffect.collectLatest {
                    when (it) {
                        LoginUiEffect.GoMainActivity -> {
                            splashScreen.setKeepOnScreenCondition { false }
                            mainNavigator.navigateFrom(
                                activity = this@LoginActivity,
                                withFinish = true,
                            )
                        }

                        LoginUiEffect.GoLoginActivity -> {
                            splashScreen.setKeepOnScreenCondition { false }
                        }
                    }
                }
            }

            YongProjectTheme {
                Scaffold { innerPadding ->
                    LoginNavHostScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel,
                    )
                }
            }
        }
    }

    private fun setObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.user.collectLatest {
                    splashScreen.setKeepOnScreenCondition { false }
                    it?.let {
                        mainNavigator.navigateFrom(
                            activity = this@LoginActivity,
                            withFinish = true,
                        )
                    }
                }
            }
        }
    }
}
