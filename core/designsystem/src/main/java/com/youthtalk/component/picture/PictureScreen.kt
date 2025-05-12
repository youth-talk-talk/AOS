package com.youthtalk.component.picture

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.youth.app.core.designsystem.R
import com.youthtalk.designsystem.gray30
import com.youthtalk.designsystem.gray80

@Composable
fun PictureScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 20.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.close),
                contentDescription = "닫기"
            )

            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "최근 항목",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                modifier = Modifier.align(Alignment.CenterEnd),
                text = "첨부",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = gray80
                )
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            item {
                Box(
                    modifier = modifier
                        .background(color = gray30)
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier.size(40.dp),
                        painter = painterResource(R.drawable.camera),
                        contentDescription = "카메라"
                    )
                }
            }

            items(
                count = 10
            ) {
                Box(
                    modifier = modifier
                        .background(color = gray30)
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text("$it")
                }
            }
        }
    }
}
