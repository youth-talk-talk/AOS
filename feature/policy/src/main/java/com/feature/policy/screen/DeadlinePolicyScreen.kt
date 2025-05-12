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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import com.feature.policy.extentions.DateUtils
import com.youth.app.feature.policy.R
import com.youthtalk.component.card.PolicyCard
import com.youthtalk.component.chip.DayChip
import com.youthtalk.component.empty.EmptyScreen
import com.youthtalk.component.topbar.MiddleTitleTopBar
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray30
import com.youthtalk.designsystem.gray40
import java.time.LocalDate

@Composable
fun DeadlinePolicyScreen(modifier: Modifier = Modifier) {
    var selectedDay by remember {
        mutableStateOf(LocalDate.now())
    }
    var count by remember {
        mutableStateOf(5)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        MiddleTitleTopBar(
            title = "마감 임박 정책",
            onBack = {}
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DateUtils.getWeeks().forEach {
                DayChip(
                    dayOfWeek = DateUtils.weekToString(it),
                    day = it.dayOfMonth,
                    isSelected = selectedDay == it,
                    onClick = { selectedDay = it }
                )
            }
        }

        HorizontalDivider(color = gray30)

        if (count != 0) {
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
        } else {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
            )
            EmptyScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(5f),
                emptyTitle = "최근 본 정책이 없어요"
            )
        }
    }
}
