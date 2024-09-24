package com.youth.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.youth.app.feature.search.R
import com.youthtalk.designsystem.gray50
import com.youthtalk.util.clickableSingle

@Composable
fun SearchBar(text: String, onChangeText: (String) -> Unit, searchDone: () -> Unit, onBack: () -> Unit, initText: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 17.dp, vertical = 3.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.clickableSingle { onBack() },
                painter = painterResource(id = R.drawable.left_icon),
                contentDescription = "뒤로가기",
                tint = Color.Black,
            )
            BasicTextField(
                value = text,
                onValueChange = onChangeText,
                textStyle = MaterialTheme.typography.titleMedium.copy(
                    color = gray50,
                ),
                singleLine = true,
                keyboardActions = KeyboardActions(
                    onSearch = { searchDone() },
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search,
                    keyboardType = KeyboardType.Text,
                ),
            ) { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            shape = RoundedCornerShape(50.dp),
                        )
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier.padding(end = 6.dp),
                        painter = painterResource(id = R.drawable.search_icon),
                        contentDescription = "검색 아이콘",
                        tint = MaterialTheme.colorScheme.onSecondary,
                    )
                    Box(modifier = Modifier.weight(1f)) {
                        innerTextField()
                    }
                    Icon(
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) {
                            initText()
                        },
                        painter = painterResource(id = R.drawable.close_icon),
                        contentDescription = "초기화",
                        tint = MaterialTheme.colorScheme.onSecondary,
                    )
                }
            }
        }
    }
}
