package com.core.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import com.youth.app.feature.home.R
import com.youthtalk.component.PolicyCheckBox
import com.youthtalk.model.Category
import kotlinx.collections.immutable.ImmutableList

@Composable
fun UpdateTitle(categoryFilters: ImmutableList<Category>, onCheck: (Category?) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
            .padding(
                horizontal = 17.dp,
                vertical = 12.dp,
            ),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = "최근 업데이트",
            style = MaterialTheme.typography.headlineSmall,
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            stringArrayResource(id = R.array.categories).forEach {
                PolicyCheckBox(
                    isCheck = categoryFilters
                        .any { category: Category -> category.categoryName == it },
                    title = it,
                    textStyle = MaterialTheme.typography.displaySmall.copy(
                        color = MaterialTheme.colorScheme.onPrimary,
                    ),
                ) {
                    onCheck(Category.entries.find { category: Category -> category.categoryName == it })
                }
            }
        }
    }
}
