package com.example.presentation.ui.login

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.presentation.R
import com.example.presentation.ui.theme.YongProjectTheme

@Composable
fun LoginScreen() {
    LoginScreen(
        onClick = {},
    )
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
    YongProjectTheme {
        LoginScreen()
    }
}
