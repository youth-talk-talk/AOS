package com.youthtalk.component.dropdown

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.youth.app.core.designsystem.R
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray100
import com.youthtalk.designsystem.gray90
import com.youthtalk.model.typeenum.SortType

@Composable
fun SortTypeDropDown(modifier: Modifier = Modifier, sortType: SortType, onClickSort: (SortType) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val title = when (sortType) {
        SortType.RECENT -> "최신순"
        SortType.POPULAR -> "인기순"
    }
    Box(
        modifier = modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                expanded = !expanded
            }
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 6.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.displayMedium
            )

            Icon(
                painter = painterResource(R.drawable.arrowdown),
                contentDescription = "아래 화살표",
                tint = gray100
            )
        }

        DropdownMenu(
            modifier = Modifier.background(color = gray10),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            SortType.entries.forEach {
                DropdownMenuItem(
                    text = {
                        Row(
                            modifier = Modifier
                                .width(100.dp)
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = when (it) {
                                    SortType.RECENT -> "최신순"
                                    SortType.POPULAR -> "인기순"
                                },
                                style = MaterialTheme.typography.displayMedium.copy(
                                    color = if (it == sortType) MaterialTheme.colorScheme.primary else gray90
                                )
                            )

                            if (it == sortType) {
                                Image(
                                    painter = painterResource(R.drawable.check),
                                    contentDescription = "체크",
                                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                                )
                            }
                        }
                    },
                    onClick = {
                        if (sortType != it) {
                            onClickSort(it)
                        }
                        expanded = !expanded
                    }
                )
            }
        }
    }
}
