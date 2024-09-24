package com.core.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.core.exception.UnAuthorizedException
import com.core.login.LoginViewModel
import com.core.navigation.LoginRouteName
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.youth.app.feature.login.R
import com.youthtalk.designsystem.YongProjectTheme
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@Composable
fun LoginScreen(navHostController: NavHostController, viewModel: LoginViewModel) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.error.collectLatest {
            when (it) {
                is UnAuthorizedException -> {
                    navHostController.navigate(LoginRouteName.AGREE_SCREEN) {
//                        popUpTo(LoginRouteName.LOGIN_SCREEN) {
//                            inclusive = true
//                        }
                        launchSingleTop = true
                    }
                }
                else -> {
                    Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
            UserApiClient.instance.logout {
                Timber.e("Kakao Login 실패")
            }
        }
    }

    LoginScreen(
        onClick = {
            // 카카오톡으로 로그인
            kakaoLogin(
                context,
                onSuccess = { userId ->
                    viewModel.postLogin(userId)
                },
            )
        },
    )
}

private fun kakaoLogin(context: Context, onSuccess: (Long) -> Unit) {
    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Timber.e(error, "카카오계정으로 로그인 실패")
        } else if (token != null) {
            Timber.i("카카오계정으로 로그인 성공 " + token.accessToken)
            UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                if (error != null) {
                    Timber.e("카카오 계정 정보 가져오기 실패")
                } else if (tokenInfo != null) {
                    onSuccess(tokenInfo.id ?: -1)
                }
            }
        }
    }

    if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
        UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
            if (error != null) {
                Timber.e(error, "카카오톡으로 로그인 실패")
                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                    return@loginWithKakaoTalk
                }

                UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
            } else if (token != null) {
                Timber.i("카카오톡으로 로그인 성공 " + token.accessToken + " " + token.accessTokenExpiresAt)
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        Timber.e(error, "사용자 정보 요청 실패")
                    } else if (user != null) {
                        onSuccess(user.id ?: -1)
                    }
                }
            }
        }
    } else {
        UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
    }
}

@Composable
fun LoginScreen(onClick: () -> Unit) {
    Surface(
        modifier =
        Modifier
            .fillMaxSize(),
    ) {
        Box(
            modifier =
            Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
        ) {
            LogoText()
            KaKaoImage(onClick = onClick)
        }
    }
}

@Composable
private fun LogoText() {
    Column(
        modifier =
        Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            modifier = Modifier.size(88.dp),
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "로고",
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.login_title),
            style = MaterialTheme.typography.labelMedium,
        )
        Text(
            text = stringResource(id = R.string.login_screen_description),
            style =
            MaterialTheme.typography.headlineLarge.copy(color = MaterialTheme.colorScheme.onSecondary),
        )
    }
}

@Composable
fun BoxScope.KaKaoImage(onClick: () -> Unit) {
    Image(
        modifier =
        Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .clickable { onClick() },
        painter =
        painterResource(
            id = R.drawable.kakao_login_large_wide,
        ),
        contentScale = ContentScale.FillWidth,
        contentDescription = stringResource(id = R.string.kakao_description),
    )
}

@Preview
@Composable
private fun LoginScreenPreview() {
    val navHostController = rememberNavController()
    val viewModel: LoginViewModel = hiltViewModel()
    YongProjectTheme {
        LoginScreen(
            navHostController,
            viewModel,
        )
    }
}
