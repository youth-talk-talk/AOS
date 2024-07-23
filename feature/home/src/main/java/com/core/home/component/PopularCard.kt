package com.core.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youth.app.feature.home.R
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.model.Category
import com.youthtalk.model.Policy

@Composable
fun PopularCard(modifier: Modifier = Modifier, policy: Policy) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(10.dp))
            .padding(horizontal = 11.dp, vertical = 15.dp),
    ) {
        Text(
            text = policy.hostDep,
            style = MaterialTheme.typography.displaySmall.copy(
                color = MaterialTheme.colorScheme.onPrimary,
            ),
        )
        Spacer(modifier = Modifier.height(1.dp))
        Text(
            modifier = Modifier.fillMaxWidth().weight(1f),
            text = policy.title,
            style = MaterialTheme.typography.bodyMedium,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = policy.category.categoryName,
                style = MaterialTheme.typography.displayMedium.copy(
                    color = MaterialTheme.colorScheme.onTertiary,
                ),
            )

            Icon(
                painter = painterResource(id = R.drawable.bookmark),
                contentDescription = "bookmark",
                tint = MaterialTheme.colorScheme.onSecondary,
            )
        }
    }
}

@Preview
@Composable
private fun PopularCardPreview() {
    val policy = Policy(
        policyId = "R2023081716945",
        category = Category.JOB,
        title = "청년 자격증 응시료 지원",
        deadlineStatus = "",
        hostDep = "인천",
        scrap = false,
    )

    YongProjectTheme {
        PopularCard(
            modifier = Modifier.aspectRatio(2f),
            policy = policy,
        )
    }
}
