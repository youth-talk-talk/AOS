package com.youthtalk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.core.community.navigation.navigateCommunityDetail
import com.core.navigation.navigator.LoginNavigator
import com.feature.policydetail.navigation.navigatePolicyDetail
import com.youthtalk.component.dialog.ModalDialog
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray100
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var loginNavigator: LoginNavigator

    private val viewModel: MainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.e("MainActivity onCreate")

        enableEdgeToEdge()
        setContent {
            YongProjectTheme {
                var notificationDialog by remember {
                    mutableStateOf(false)
                }
                LaunchedEffect(viewModel.effect) {
                    viewModel.effect.collectLatest {
                        when (it) {
                            is MainUiEffect.Notification -> {
                                Timber.e("MainUiEffect Notification")
                                notificationDialog = true
                            }
                        }
                    }
                }

                val scope = rememberCoroutineScope()
                val snackbarHostState = remember { SnackbarHostState() }
                var isNavigateTriggered by remember { mutableStateOf(false) }
                Box {
                    Scaffold(
                        snackbarHost = {
                            AnimatedCustomSnackbarHost(snackbarHostState)
                        }
                    ) { innerPadding ->
                        val navHostController = rememberNavController()
                        val currentRoute by navHostController.currentBackStackEntryAsState()
                        Timber.e("main currentRoute $currentRoute")
                        LaunchedEffect(currentRoute) {
                            if (!isNavigateTriggered) {
                                currentRoute?.destination?.route?.let {
                                    intent.getLongExtra("postId", -1L).let { postId ->
                                        if (postId != -1L) {
                                            navHostController.navigateCommunityDetail(postId)
                                        }
                                    }

                                    intent.getLongExtra("policyId", -1L).let { policyId ->
                                        if (policyId != -1L) {
                                            navHostController.navigatePolicyDetail(policyId)
                                        }
                                    }
                                    isNavigateTriggered = true
                                }
                            }
                        }
                        MainScreen(
                            modifier = Modifier.padding(innerPadding),
                            navController = navHostController,
                            goLogin = {
                                loginNavigator.navigateFrom(
                                    activity = this@MainActivity,
                                    withFinish = true
                                )
                            },
                            checkPermission = { permission ->
                                ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity, permission)
                            },
                            showSnackBar = { message ->
                                scope.launch {
                                    snackbarHostState.showSnackbar(message)
                                }
                            }
                        )
                    }
                    CustomStatusBarColor(color = Color.Transparent)
                }

                if (notificationDialog) {
                    ModalDialog(
                        title = "알림 설정",
                        subTitle = "마이페이지를 통해 알림 권한을 변경할 수 있습니다.",
                        confirmText = "확인",
                        cancelText = "",
                        onDismissRequest = { notificationDialog = false }
                    )
                }
            }
        }
    }

    @Composable
    fun AnimatedCustomSnackbarHost(hostState: SnackbarHostState) {
        var visible by remember { mutableStateOf(false) }

        LaunchedEffect(hostState.currentSnackbarData) {
            Timber.e("Show Snackbar ${hostState.currentSnackbarData}")
            visible = hostState.currentSnackbarData != null
            hostState.currentSnackbarData?.let { snackbarData ->
                val time = when (snackbarData.visuals.duration) {
                    SnackbarDuration.Indefinite -> Long.MAX_VALUE
                    SnackbarDuration.Long -> 10000L
                    SnackbarDuration.Short -> 4000L
                }
                delay(time)
                visible = false
                snackbarData.dismiss()
            }
        }

        Box(
            modifier = Modifier
                .padding(bottom = 80.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut()
            ) {
                hostState.currentSnackbarData?.let { snackbarData ->
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = gray100,
                        modifier = Modifier
                            .padding(horizontal = 16.dp),
                        tonalElevation = 6.dp,
                        shadowElevation = 6.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = snackbarData.visuals.message,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = gray10
                                )
                            )
                        }
                    }
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
                .background(color)
        )
    }
}
