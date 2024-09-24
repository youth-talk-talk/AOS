package com.core.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray
import com.youthtalk.designsystem.gray50

@Composable
fun DropDownComponent(
    modifier: Modifier = Modifier,
    dropDownList: List<String> = listOf(),
    dropDownClick: (String) -> Unit,
    selectedRegion: String,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var expanded by remember {
        mutableStateOf(false)
    }

    var rowSize by remember { mutableStateOf(Size.Zero) }

    Column(
        modifier =
        modifier
            .onGloballyPositioned { rowSize = it.size.toSize() },
    ) {
        DropBar(expanded = expanded, selectedRegion = selectedRegion) {
            keyboardController?.hide()
            expanded = true
        }

        DropdownMenu(
            modifier =
            Modifier
                .width(with(LocalDensity.current) { rowSize.width.toDp() })
                .height(300.dp)
                .background(MaterialTheme.colorScheme.background),
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            dropDownList.forEach {
                Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = gray50,
                                ),
                            )
                        },
                        onClick = {
                            dropDownClick(it)
                            expanded = false
                        },
                    )
                    HorizontalDivider(color = Color(0xFF72777A))
                }
            }
        }
    }
}

@Composable
fun DropBar(expanded: Boolean, selectedRegion: String, onClick: () -> Unit) {
    Box(
        modifier =
        Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
            .border(
                shape = RoundedCornerShape(8.dp),
                width = 1.dp,
                color = gray,
            )
            .padding(horizontal = 13.dp, vertical = 10.dp)
            .clickable { onClick() },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = selectedRegion,
                modifier =
                Modifier
                    .weight(1f),
                style = if (selectedRegion == "전체지역") {
                    MaterialTheme.typography.displayMedium.copy(Color.Gray)
                } else {
                    MaterialTheme.typography.displayMedium.copy(Color.Black)
                },
            )

            Icon(
                modifier = Modifier.size(24.dp),
                imageVector =
                if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = "키 다운",
                tint = Color.Gray,
            )
        }
    }
}

@Preview
@Composable
private fun DropDownComponentPreview() {
    YongProjectTheme {
        DropDownComponent(
            dropDownClick = {},
            selectedRegion = "",
            dropDownList = listOf(),
        )
    }
}
