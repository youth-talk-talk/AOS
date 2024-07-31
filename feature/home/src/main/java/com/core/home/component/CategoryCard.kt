package com.core.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.youth.app.feature.home.R
import com.youthtalk.designsystem.YongProjectTheme

@Composable
fun CategoryCard(modifier: Modifier = Modifier, category: String, painter: Painter) {
    Column(
        modifier = modifier.background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        Icon(
            modifier = Modifier
                .padding(top = 9.dp)
                .padding(horizontal = 9.dp)
                .size(33.dp),
            painter = painter,
            contentDescription = category,
            tint = MaterialTheme.colorScheme.primary,
        )
        Text(
            modifier = Modifier.padding(bottom = 7.dp),
            text = category,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.W700,
                fontSize = 14.sp,
                lineHeight = 16.sp,
            ),
        )
    }
}

@Preview
@Composable
private fun CategoryCardPreview() {
    YongProjectTheme {
        CategoryCard(
            category = "교육",
            painter = painterResource(id = R.drawable.teacher),
        )
    }
}
