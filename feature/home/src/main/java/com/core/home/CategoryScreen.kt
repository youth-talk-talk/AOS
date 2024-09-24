package com.core.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.core.home.component.CategoryCard
import com.youth.app.feature.home.R
import com.youthtalk.model.Category
import com.youthtalk.util.clickableSingle

@Composable
fun CategoryScreen(goSpecPolicyScreen: (Category) -> Unit) {
    Row(
        modifier =
        Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Category.entries.forEach { category ->
            CategoryCard(
                modifier = Modifier
                    .clickableSingle {
                        goSpecPolicyScreen(category)
                    },
                category = category.categoryName,
                painter = when (category) {
                    Category.JOB -> painterResource(id = R.drawable.recruit)
                    Category.EDUCATION -> painterResource(id = R.drawable.teacher)
                    Category.LIFE -> painterResource(id = R.drawable.book)
                    Category.PARTICIPATION -> painterResource(id = R.drawable.participation)
                },
            )
        }
    }
}
