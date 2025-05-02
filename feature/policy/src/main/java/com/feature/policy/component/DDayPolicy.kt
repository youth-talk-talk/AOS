package com.feature.policy.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.feature.policy.extentions.DateUtils
import com.youthtalk.component.card.PolicyCard
import com.youthtalk.component.chip.DayChip
import com.youthtalk.component.item.TitleItem
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray30
import com.youthtalk.designsystem.gray40
import java.time.LocalDate

@Composable
fun DDayPolicy(modifier: Modifier = Modifier, selectedDay: LocalDate, onClickDay: (LocalDate) -> Unit) {
    Column(modifier = modifier) {
        TitleItem(
            title = "곧 마감되니 서둘러 지원해 보세요!",
            onClick = {},
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            DateUtils.getWeeks().forEach {
                DayChip(
                    dayOfWeek = DateUtils.weekToString(it),
                    day = it.dayOfMonth,
                    isSelected = selectedDay == it,
                    onClick = { onClickDay(it) },
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 38.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            repeat(5) {
                PolicyCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = gray10,
                            shape = RoundedCornerShape(12.dp),
                        )
                        .border(
                            width = 1.dp,
                            color = gray40,
                            shape = RoundedCornerShape(12.dp),
                        ),
                )
            }
        }

        HorizontalDivider(
            thickness = 10.dp,
            color = gray30,
        )
    }
}
