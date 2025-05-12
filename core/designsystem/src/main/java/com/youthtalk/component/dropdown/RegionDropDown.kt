package com.youthtalk.component.dropdown

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray100
import com.youthtalk.designsystem.gray50
import com.youthtalk.designsystem.gray60

@Composable
fun RegionDropDown(modifier: Modifier = Modifier, select: String = "", hint: String = "", onSelect: () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 46.dp)
            .background(
                color = gray10,
                shape = RoundedCornerShape(6.dp)
            )
            .border(
                width = 1.dp,
                color = gray50,
                shape = RoundedCornerShape(6.dp)
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onSelect()
            }
            .padding(vertical = 10.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = if (select.isEmpty()) hint else select,
            style = MaterialTheme.typography.titleSmall.copy(
                color = when (select.isEmpty()) {
                    true -> gray60
                    false -> gray100
                }
            )
        )

        Icon(
            modifier = Modifier.padding(start = 10.dp),
            painter = painterResource(R.drawable.arrowdown),
            contentDescription = "펼치기",
            tint = gray60
        )
    }
}

@Preview
@Composable
private fun CustomDropDownPreview() {
    YongProjectTheme {
        RegionDropDown(
            select = "",
            hint = "지역을 선택해 주세요",
            onSelect = {}
        )
    }
}
