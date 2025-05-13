package com.youth.search.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.youth.app.feature.search.R
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray30
import com.youthtalk.designsystem.gray70

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    text: String = "",
    hint: String = "검색어를 입력해주세요.",
    onClickBack: () -> Unit,
    onTextChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onClear: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = gray10)
            .padding(horizontal = 16.dp)
            .padding(top = 24.dp, bottom = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    onClickBack()
                },
            painter = painterResource(R.drawable.arrowleft),
            contentDescription = "왼쪽 화살표"
        )

        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = gray30,
                    shape = RoundedCornerShape(6.dp)
                )
                .padding(horizontal = 14.dp, vertical = 10.dp),
            value = text,
            onValueChange = onTextChange,
            textStyle = MaterialTheme.typography.titleSmall,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onSearch(text)
                    focusManager.clearFocus()
                }
            )
        ) { innerTextField ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(R.drawable.search),
                    contentDescription = "검색",
                    colorFilter = ColorFilter.tint(color = gray70)
                )

                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    if (text.isEmpty()) {
                        Text(
                            text = hint,
                            style = MaterialTheme.typography.titleSmall.copy(
                                color = gray70
                            )
                        )
                    }
                    innerTextField()
                }

                if (text.isNotEmpty()) {
                    Image(
                        modifier = Modifier
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                onClear()
                            }
                            .padding(start = 14.dp),
                        painter = painterResource(R.drawable.closecircle),
                        contentDescription = "초기화"
                    )
                }
            }
        }
    }
}
