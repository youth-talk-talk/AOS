package com.core.screen

import android.content.Context
import android.util.Log
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.core.login.LoginViewModel
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.youth.app.feature.login.R
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.model.Token
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    val context = LocalContext.current

    val token by viewModel.token.collectAsState()

    LaunchedEffect(key1 = viewModel.error) {
        viewModel.error.collectLatest {
            Log.d("YOON-CHAN", "LOGINSCREEN $it")
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
        token,
    )
}

private fun kakaoLogin(context: Context, onSuccess: (Long) -> Unit) {
// 카카오계정으로 로그인 공통 callback 구성
// 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e("LOGINSCREEN", "카카오계정으로 로그인 실패", error)
        } else if (token != null) {
            Log.i("LOGINSCREEN", "카카오계정으로 로그인 성공 ${token.accessToken}")
            // 토큰 정보 보기
            UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                if (error != null) {
                    Log.d("LOGINSCREEN", "카카오 계정 정보 가져오기 실패")
                } else if (tokenInfo != null) {
                    onSuccess(tokenInfo.id ?: -1)
                }
            }
        }
    }

// 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
    if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
        UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
            if (error != null) {
                Log.e("LOGINSCREEN", "카카오톡으로 로그인 실패", error)

                // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                    return@loginWithKakaoTalk
                }

                // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
            } else if (token != null) {
                Log.i("LOGINSCREEN", "카카오톡으로 로그인 성공 ${token.accessToken} ${token.accessTokenExpiresAt}")
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        Log.e("LOGINSCREEN", "사용자 정보 요청 실패", error)
                    } else if (user != null) {
                        Log.i(
                            "LOGINSCREEN",
                            "사용자 정보 요청 성공" +
                                "\n회원번호: ${user.id}" +
                                "\n이메일: ${user.kakaoAccount?.email}" +
                                "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                                "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}",
                        )

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
fun LoginScreen(onClick: () -> Unit, token: Token?) {
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
            LogoText(token)
            KaKaoImage(onClick = onClick)
        }
    }
}

@Composable
private fun LogoText(token: Token?) {
    Column(
        modifier =
        Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            modifier = Modifier.size(88.dp),
            imageVector = Icons.Default.AddCircle,
            contentDescription = "로고",
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
        )
        Text(
            text = stringResource(id = R.string.login_screen_description),
            style =
            MaterialTheme.typography.bodySmall
                .copy(color = Color.Black),
        )

        Text(text = "${token?.accessToken} ${token?.refreshToken}")
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
    val viewModel: LoginViewModel = hiltViewModel()
    YongProjectTheme {
        LoginScreen(viewModel)
    }
}
