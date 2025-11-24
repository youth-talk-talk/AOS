package com.core.screen

import android.os.Build.VERSION.SDK_INT
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
import com.youth.app.feature.login.R
import com.youthtalk.component.dialog.ModalDialog
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray100

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

    ModalDialog(
        title = "내부 사정으로 인해 더 이상 청년톡톡 지원이 불가합니다.",
        subTitle = "그 동안 청년톡톡을 사랑해주셔서 감사합니다.",
        cancelText = "",
        confirmText = "종료하기",
        onDismissRequest = {},
        onClickConfirm = {
            onClick()
        }
    )
    Surface(
        modifier =
        Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp, bottom = 40.dp)
                    .padding(horizontal = 32.dp),
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context).data(R.drawable.login_image).build(),
                    imageLoader = imageLoader
                ),
                contentDescription = null
            )

            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = stringResource(id = R.string.login_title),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Text(
                text = stringResource(id = R.string.login_screen_description),
                style =
                MaterialTheme.typography.titleMedium.copy(color = gray100)
            )
            KaKaoImage(
                modifier = Modifier.padding(top = 70.dp),
                onClick = onClick
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
            id = R.drawable.kakao_large_wide_kr
        ),
        contentScale = ContentScale.FillWidth,
        contentDescription = stringResource(id = R.string.kakao_description)
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
