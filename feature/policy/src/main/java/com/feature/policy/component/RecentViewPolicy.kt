package com.feature.policy.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.youthtalk.component.card.PolicyCard
import com.youthtalk.component.item.TitleItem
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray30
import com.youthtalk.designsystem.gray40
import com.youthtalk.model.policy.Policy

@Composable
fun RecentViewPolicy(
    modifier: Modifier = Modifier,
    policies: List<Policy>,
    onClickRecentViewPolicy: () -> Unit,
    onClickPolicyDetail: (Long) -> Unit
) {
    Column(
        modifier = modifier
            .padding(top = 10.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        TitleItem(
            title = "최근 본 정책",
            onClick = onClickRecentViewPolicy
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                count = policies.size.coerceAtMost(10)
            ) {
                PolicyCard(
                    modifier = Modifier
                        .width(300.dp)
                        .background(
                            color = gray10,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = gray40,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    policy = policies[it],
                    onClick = { onClickPolicyDetail(policies[it].policyId) },
                    onClickScrap = { id, scrap -> }
                )
            }
        }

        HorizontalDivider(
            thickness = 10.dp,
            color = gray30
        )
    }
}
