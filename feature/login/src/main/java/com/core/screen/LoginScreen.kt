package com.core.screen

import android.content.Context
import android.os.Build.VERSION.SDK_INT
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.ImageLoader
import coil3.compose.rememberAsyncImagePainter
import coil3.gif.AnimatedImageDecoder
import coil3.gif.GifDecoder
import coil3.request.ImageRequest
import com.core.exception.UnAuthorizedException
import com.core.login.LoginViewModel
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.youth.app.feature.login.R
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@Composable
fun LoginScreen(viewModel: LoginViewModel, goAgreeScreen: () -> Unit) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.error.collectLatest {
            when (it) {
                is UnAuthorizedException -> {
                    goAgreeScreen()
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
    val context = LocalContext.current

    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(AnimatedImageDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    Surface(
        modifier =
        Modifier
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp, bottom = 40.dp)
                    .padding(horizontal = 32.dp),
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context).data(R.drawable.login_image).build(),
                    imageLoader = imageLoader,
                ),
                contentDescription = null,
            )

            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = stringResource(id = R.string.login_title),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                ),
            )

            Text(
                text = stringResource(id = R.string.login_screen_description),
                style =
                MaterialTheme.typography.titleMedium.copy(color = gray),
            )
            KaKaoImage(
                modifier = Modifier.padding(top = 70.dp),
                onClick = onClick,
            )
        }
    }
}

@Composable
fun KaKaoImage(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Image(
        modifier =
        modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onClick() },
        painter =
        painterResource(
            id = R.drawable.kakao_large_wide_kr,
        ),
        contentScale = ContentScale.FillWidth,
        contentDescription = stringResource(id = R.string.kakao_description),
    )
}

@Preview
@Composable
private fun LoginScreenPreview() {
    YongProjectTheme {
        LoginScreen {
        }
    }
}
