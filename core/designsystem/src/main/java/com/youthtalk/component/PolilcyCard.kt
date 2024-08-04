package com.youthtalk.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youth.app.core.designsystem.R
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.model.Category
import com.youthtalk.model.Policy

@Composable
fun PolicyCard(modifier: Modifier = Modifier, policy: Policy) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(10.dp),
            )
            .padding(horizontal = 11.dp, vertical = 13.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = policy.hostDep,
                    style = MaterialTheme.typography.displaySmall,
                )
                if (policy.deadlineStatus.isNotEmpty()) {
                    Text(
                        text = policy.deadlineStatus,
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.onTertiary,
                        ),
                    )
                }
            }
            Text(
                modifier = Modifier,
                text = policy.title,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Text(
                modifier = Modifier.align(Alignment.BottomStart),
                text = policy.category.categoryName,
                style = MaterialTheme.typography.displayMedium.copy(
                    color = MaterialTheme.colorScheme.onTertiary,
                ),
            )
            Image(
                modifier = Modifier.align(Alignment.BottomEnd),
                painter = painterResource(id = R.drawable.bookmark),
                contentDescription = "스크랩 이미지",
            )
        }
    }
}

@Preview
@Composable
private fun PolicyCardPreview() {
    val policy = Policy(
        policyId = "R2023081716945",
        category = Category.JOB,
        title = "국민 취업지원 제도",
        deadlineStatus = "123",
        hostDep = "국토교통부",
        scrap = false,
    )

    YongProjectTheme {
        PolicyCard(
            policy = policy,
        )
    }
}
