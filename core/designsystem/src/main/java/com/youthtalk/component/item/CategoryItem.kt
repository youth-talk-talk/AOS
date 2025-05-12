package com.youthtalk.component.item

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youth.app.core.designsystem.R
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray30
import com.youthtalk.designsystem.gray90
import com.youthtalk.model.Category

@Composable
fun CategoryItem(modifier: Modifier = Modifier, category: Category, isSelected: Boolean = false, onClick: () -> Unit) {
    val painter = painterResource(
        when (category) {
            Category.ALL -> R.drawable.category_all
            Category.DWELLING -> R.drawable.category_home
            Category.EDUCATION -> R.drawable.category_education
            Category.JOB -> R.drawable.category_job
            Category.LIFE -> R.drawable.category_life
            Category.PARTICIPATION -> R.drawable.category_participation
        }
    )

    Column(
        modifier = modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick()
            },
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = gray30,
                    shape = RoundedCornerShape(8.73.dp)
                )
                .padding(10.2.dp)
        ) {
            Image(
                painter = painter,
                contentDescription = "카테고리 이미지"
            )
        }

        Text(
            text = category.categoryName,
            style = MaterialTheme.typography.displayLarge.copy(
                color = if (isSelected) MaterialTheme.colorScheme.primary else gray90
            )
        )
    }
}

@Preview
@Composable
private fun CategoryItemPreview() {
    YongProjectTheme {
        CategoryItem(
            category = Category.DWELLING
        ) { }
    }
}

@Preview
@Composable
private fun CategoryItemIsSelectedPreview() {
    YongProjectTheme {
        CategoryItem(
            category = Category.DWELLING,
            isSelected = true
        ) { }
    }
}
