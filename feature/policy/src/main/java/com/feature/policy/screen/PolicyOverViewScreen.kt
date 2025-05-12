package com.feature.policy.screen

import android.icu.text.DecimalFormat
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.youth.app.feature.policy.R
import com.youthtalk.component.card.PolicyCard
import com.youthtalk.component.chip.FilterChip
import com.youthtalk.component.item.CategoryItem
import com.youthtalk.component.topbar.MiddleTitleTopBar
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray30
import com.youthtalk.designsystem.gray40
import com.youthtalk.model.Category
import com.youthtalk.model.FilterType

@Composable
fun PolicyOverviewScreen(modifier: Modifier = Modifier) {
    var bottomSheet by remember {
        mutableStateOf(false)
    }
    var count by remember {
        mutableStateOf(10)
    }
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        MiddleTitleTopBar(
            title = "정책 보아보기",
            onBack = {}
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(
                items = Category.entries.toList()
            ) {
                CategoryItem(
                    category = it,
                    onClick = {}
                )
            }
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 15.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                count = FilterType.entries.toList().size
            ) {
                val filters = FilterType.entries.toList()

                val title = when (filters[it]) {
                    FilterType.POLICY_TYPE -> "정책분야"
                    FilterType.REGION -> "지역"
                    FilterType.RECRUIT -> "취업상태"
                    FilterType.EDUCATION -> "학력"
                    FilterType.SPECIALIZED -> "특화 분야"
                    FilterType.AGE_EARN -> "연령 및 소득"
                }

                FilterChip(
                    text = title,
                    onClick = {
                        bottomSheet = true
                    }
                )
            }
        }

        HorizontalDivider(
            color = gray30
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                count = count
            ) {
                if (it == 0) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "총 ${DecimalFormat("#,###").format(count)}건",
                            style = MaterialTheme.typography.displayMedium
                        )

                        Row(
                            modifier = Modifier
                                .padding(horizontal = 6.dp, vertical = 2.dp),
                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "최신순",
                                style = MaterialTheme.typography.displayMedium
                            )

                            Image(
                                modifier = Modifier.size(16.dp),
                                painter = painterResource(R.drawable.arrowdown),
                                contentDescription = "아래 화살표"
                            )
                        }
                    }
                }

                PolicyCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = gray10,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = gray40,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            count--
                        }
                )
            }
        }
    }
}
