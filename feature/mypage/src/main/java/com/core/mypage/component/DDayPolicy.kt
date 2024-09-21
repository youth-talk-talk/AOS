package com.core.mypage.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youthtalk.component.PolicyCard
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.model.Category
import com.youthtalk.model.Policy

@Composable
fun DDayPolicy(modifier: Modifier = Modifier, policies: List<Policy>, onClickDetailPolicy: (String) -> Unit) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
            .background(
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(vertical = 13.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 17.dp),
            text = "마감 D-Day",
            style = MaterialTheme.typography.headlineSmall,
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            contentPadding = PaddingValues(horizontal = 17.dp),
        ) {
            items(
                count = policies.size,
                key = { index -> policies[index].policyId },
            ) { index ->
                PolicyCard(
                    modifier = Modifier.aspectRatio(2.5f),
                    policy = policies[index],
                    onClickDetailPolicy = onClickDetailPolicy,
                    onClickScrap = { _, _ -> },
                )
            }
        }
    }
}

@Preview
@Composable
private fun DDayPolicyPreview() {
    YongProjectTheme {
        DDayPolicy(
            modifier = Modifier.height(160.dp),
            policies = listOf(
                Policy(
                    policyId = "R2023081716945",
                    category = Category.JOB,
                    title = "국민 취업지원 제도",
                    deadlineStatus = "",
                    hostDep = "국토교통부",
                    scrap = false,
                ),
                Policy(
                    policyId = "R2023081716946",
                    category = Category.JOB,
                    title = "국민 취업지원 제도",
                    deadlineStatus = "",
                    hostDep = "국토교통부",
                    scrap = false,
                ),
                Policy(
                    policyId = "R2023081716947",
                    category = Category.JOB,
                    title = "국민 취업지원 제도",
                    deadlineStatus = "",
                    hostDep = "국토교통부",
                    scrap = false,
                ),
                Policy(
                    policyId = "R2023081716948",
                    category = Category.JOB,
                    title = "국민 취업지원 제도",
                    deadlineStatus = "",
                    hostDep = "국토교통부",
                    scrap = false,
                ),
            ),
            onClickDetailPolicy = {},
        )
    }
}
