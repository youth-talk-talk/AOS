package com.youth.search.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.youth.app.feature.search.R
import com.youthtalk.component.card.PostCard
import com.youthtalk.designsystem.gray40
import com.youthtalk.model.Category
import com.youthtalk.model.CommunityType
import java.text.DecimalFormat

@Composable
fun CommunitySearchResultScreen(modifier: Modifier = Modifier, communityType: CommunityType) {
    var count by remember {
        mutableStateOf(10)
    }
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "총 ${DecimalFormat("#,###").format(count)}건",
                    style = MaterialTheme.typography.displayMedium,
                )

                Row(
                    modifier = Modifier
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "최신순",
                        style = MaterialTheme.typography.displayMedium,
                    )

                    Image(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(R.drawable.arrowdown),
                        contentDescription = "아래 화살표",
                    )
                }
            }
        }

        items(
            count = count,
        ) {
            PostCard(
                modifier = Modifier.padding(horizontal = 16.dp),
                keyword = when (communityType) {
                    CommunityType.REVIEW -> Category.PARTICIPATION.categoryName.split(" ").first()
                    CommunityType.FREE -> ""
                },
                communityTitle = "영화 보는 거 좋아하는 사람? 꿀팁 알려드림!",
                communitySubTitle = "영화 보는 거 좋아하는 사람? 꿀팁 알려드림! 영화 보는 거 좋아하는 사람...",
                policyTitle = when (communityType) {
                    CommunityType.REVIEW -> "청년문화예술패스"
                    CommunityType.FREE -> ""
                },
                onClick = {},
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp),
                color = gray40,
            )
        }
    }
}
