package com.youthtalk.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.youth.app.core.designsystem.R
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray
import com.youthtalk.util.clickableSingle

@Composable
fun SortDropDownComponent(modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = modifier,
    ) {
        DropBar {
            expanded = true
        }
        MaterialTheme(
            shapes = MaterialTheme.shapes.copy(
                extraSmall = RoundedCornerShape(0.dp),
                medium = RoundedCornerShape(0.dp),
            ),
        ) {
            DropdownMenuNoPaddingVertical(
                modifier = Modifier
                    .padding(PaddingValues(0.dp))
                    .background(MaterialTheme.colorScheme.background),
                expanded = expanded,
                onDismissRequest = { expanded = false },
                offset = DpOffset(0.dp, 5.dp),
            ) {
                val list = listOf("최신순", "마감순")
                list.forEachIndexed { index, s ->
                    DropdownMenuItem(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background),
                        contentPadding = PaddingValues(0.dp),
                        text = {
                            Text(
                                modifier = Modifier
                                    .padding(start = 15.dp),
                                text = s,
                                style = MaterialTheme.typography.titleMedium,
                            )
                        },
                        onClick = { /* TODO */ },
                    )
                    if (index != list.size - 1) {
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
fun DropBar(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Row(
        modifier = modifier
            .size(
                width = 80.dp,
                height = 25.dp,
            )
            .border(
                width = 1.dp,
                color = gray,
                shape = RoundedCornerShape(50.dp),
            )
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(50.dp),
            )
            .padding(
                start = 13.dp,
                end = 6.dp,
                top = 5.dp,
                bottom = 5.dp,
            )
            .clickableSingle {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = "최신순",
            style = MaterialTheme.typography.headlineLarge,
        )
        Icon(
            painter = painterResource(id = R.drawable.dropdown_up_icon),
            contentDescription = "드롭다운 업",
            tint = Color.Black,
        )
    }
}

@Preview
@Composable
private fun SortDropDownComponentPreview() {
    YongProjectTheme {
        SortDropDownComponent()
    }
}
