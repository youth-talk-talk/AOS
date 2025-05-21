package com.feature.policy.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.youthtalk.component.card.PolicyCard
import com.youthtalk.component.empty.EmptyScreen
import com.youthtalk.component.topbar.MiddleTitleTopBar
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray40
import com.youthtalk.designsystem.gray80
import com.youthtalk.model.policy.Policy
import com.youthtalk.model.policy.PolicyType
import com.youthtalk.model.typeenum.Category

@Composable
fun RecentlyViewPolicyScreen(modifier: Modifier = Modifier) {
    var count by remember {
        mutableStateOf(5)
    }
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        MiddleTitleTopBar(
            title = "최근 본 정책",
            onBack = {},
            tails = {
                Text(
                    text = "전체 선택",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = gray80
                    )
                )
            }
        )

        if (count == 0) {
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
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    count = count
                ) {
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
                            },
                        policy = Policy(
                            policyId = 0,
                            category = Category.ALL,
                            title = "",
                            deadlineStatus = "",
                            hostDep = "",
                            scrapCount = 0,
                            departmentImgUrl = null,
                            region = "",
                            scrap = false,
                            policyType = PolicyType.SEARCH
                        ),
                        onClickScrap = { id, scrap -> }
                    )
                }
            }
        }
    }
}
