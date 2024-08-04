package com.youthtalk.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youth.app.core.designsystem.R
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray

@Composable
fun PolicyCheckBox(
    modifier: Modifier = Modifier,
    isCheck: Boolean,
    title: String,
    textStyle: TextStyle,
    spaceBy: Arrangement.HorizontalOrVertical = Arrangement.spacedBy(3.dp),
    onCheckChange: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = spaceBy,
    ) {
        Box(
            modifier = Modifier
                .size(15.dp)
                .isChecked(isCheck)
                .clickable {
                    onCheckChange()
                },
        ) {
            if (isCheck) {
                Icon(
                    modifier = Modifier.align(Alignment.Center),
                    painter = painterResource(id = R.drawable.check_icon),
                    contentDescription = "체크",
                    tint = Color.White,
                )
            }
        }
        Text(
            text = title,
            style = textStyle,
        )
    }
}

@Composable
private fun Modifier.isChecked(isCheck: Boolean) = if (isCheck) {
    this.background(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(4.dp))
} else {
    this.border(
        width = 1.dp,
        color = gray,
        shape = RoundedCornerShape(4.dp),
    )
}

@Preview
@Composable
private fun PolicyCheckBoxPreview() {
    var isCheck by remember {
        mutableStateOf(false)
    }
    YongProjectTheme {
        PolicyCheckBox(
            isCheck = isCheck,
            title = "일자리",
            spaceBy = Arrangement.spacedBy(3.dp),
            textStyle = MaterialTheme.typography.displaySmall.copy(
                color = MaterialTheme.colorScheme.onPrimary,
            ),
            onCheckChange = {
                isCheck = !isCheck
            },
        )
    }
}
