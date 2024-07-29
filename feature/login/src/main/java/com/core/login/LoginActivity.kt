package com.core.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.core.screen.LoginNavHostScreen
import com.youthtalk.MainActivity
import com.youthtalk.designsystem.YongProjectTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var splashScreen: SplashScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { true }
        setObserver()

        setContent {
            YongProjectTheme {
                LoginNavHostScreen(viewModel)
            }
        }
    }

    private fun setObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.memberId.collectLatest {
                    splashScreen.setKeepOnScreenCondition { false }
                    Log.d("YOON-CHAN", "MainActivity memberId $it")
                    goToMain()
//                    UserApiClient.instance.logout { logout ->
//                        if (logout != null) {
//                            Log.d("YOON-CHAN", "Kakao Logout 실패 ${logout.message}")
//                        } else {
//                            Log.d("YOON-CHAN", "Kakao Logout 성공")
//                        }
//                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.user.collectLatest {
                    splashScreen.setKeepOnScreenCondition { false }
                    it?.let { goToMain() }
                }
            }
        }
    }

    private fun goToMain() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
