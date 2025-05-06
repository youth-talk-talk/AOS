package com.core.community.screen.write

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.youth.app.feature.community.R
import com.youthtalk.component.topbar.NoBackMiddleTitleTopBar
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray100
import com.youthtalk.designsystem.gray30
import com.youthtalk.designsystem.gray40
import com.youthtalk.designsystem.gray70
import com.youthtalk.designsystem.gray80

@Composable
fun PolicySearchScreen(modifier: Modifier = Modifier) {
    var policyName by remember {
        mutableStateOf("")
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = gray10),
    ) {
        NoBackMiddleTitleTopBar(
            title = "정책 검색",
            tails = {
                Image(
                    painter = painterResource(R.drawable.close),
                    contentDescription = "닫기",
                    colorFilter = ColorFilter.tint(color = gray100),
                )
            },
        )

        BasicTextField(
            modifier = Modifier.padding(horizontal = 16.dp),
            value = policyName,
            onValueChange = { policyName = it },
            textStyle = MaterialTheme.typography.titleSmall,
        ) { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = gray30,
                        shape = RoundedCornerShape(6.dp),
                    )
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(R.drawable.search),
                    contentDescription = "검색",
                    colorFilter = ColorFilter.tint(color = gray70),
                )

                Box(modifier = Modifier.weight(1f)) {
                    if (policyName.isEmpty()) {
                        Text(
                            text = "정책명을 검색해 주세요",
                            style = MaterialTheme.typography.titleSmall.copy(
                                color = gray70,
                            ),
                        )
                    }
                    innerTextField()
                }

                if (policyName.isNotEmpty()) {
                    Image(
                        modifier = Modifier
                            .padding(start = 4.dp),
                        painter = painterResource(R.drawable.closecircle),
                        contentDescription = "초기화",
                    )
                }
            }
        }

        if (policyName.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "후기를 작성할 정책을 검색헤 보세요.",
                    style = MaterialTheme.typography.displaySmall.copy(
                        color = gray80,
                    ),
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                items(count = 10) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = if (it == 2) MaterialTheme.colorScheme.onPrimary else gray10,
                            )
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            painter = painterResource(R.drawable.policy),
                            contentDescription = "정책아이콘",
                        )

                        Text(
                            text = "청년 문화예술패스",
                            style = MaterialTheme.typography.displaySmall,
                        )
                    }

                    if (it != 9) {
                        HorizontalDivider(
                            color = gray40,
                        )
                    }
                }
            }
        }
    }
}
