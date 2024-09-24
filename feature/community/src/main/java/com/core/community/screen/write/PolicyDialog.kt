package com.core.community.screen.write

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.paging.compose.LazyPagingItems
import com.youth.app.feature.community.R
import com.youthtalk.component.RoundButton
import com.youthtalk.designsystem.gray50
import com.youthtalk.model.SearchPolicy

@Composable
fun PolicyDialog(policies: LazyPagingItems<SearchPolicy>, onDismiss: () -> Unit, onSearch: (String) -> Unit, onClick: (SearchPolicy?) -> Unit) {
    var policyText by remember {
        mutableStateOf("")
    }
    var selectSearchPolicy by remember {
        mutableStateOf<SearchPolicy?>(null)
    }
    Dialog(onDismissRequest = { onDismiss() }) {
        val focus = LocalFocusManager.current
        val ime = LocalSoftwareKeyboardController.current

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(525.dp)
                .background(
                    MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(20.dp),
                )
                .padding(vertical = 20.dp, horizontal = 10.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "정책검색",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onSecondary,
                    ),
                )

                Icon(
                    modifier = Modifier
                        .align(Alignment.CenterEnd),
                    painter = painterResource(id = R.drawable.close_icon),
                    contentDescription = "닫기",
                    tint = Color.Black,
                )
            }

            BasicTextField(
                value = policyText,
                onValueChange = { policyText = it },
                singleLine = true,
                keyboardActions = KeyboardActions(
                    onSearch = {
                        focus.clearFocus()
                        ime?.hide()
                        onSearch(policyText)
                    },
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search,
                ),
            ) { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            shape = RoundedCornerShape(50.dp),
                        )
                        .padding(horizontal = 20.dp, vertical = 17.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (policyText.isEmpty()) {
                            Text(
                                text = "정책명을 검색해주세요",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = gray50,
                                ),
                            )
                        } else {
                            innerTextField()
                        }
                    }
                    Icon(
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) {
                            focus.clearFocus()
                            ime?.hide()
                            onSearch(policyText)
                        },
                        painter = painterResource(id = R.drawable.search_icon),
                        contentDescription = "검색",
                        tint = Color.Black,
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.background),
            ) {
                items(
                    count = policies.itemCount,
                    key = { index -> policies.peek(index)!!.policyId },
                ) { index ->
                    policies[index]?.let {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    if (it == selectSearchPolicy) {
                                        MaterialTheme.colorScheme.primary.copy(
                                            alpha = 0.5f,
                                        )
                                    } else {
                                        MaterialTheme.colorScheme.background
                                    },
                                )
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                ) {
                                    selectSearchPolicy = if (it == selectSearchPolicy) null else it
                                },
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(start = 15.dp, top = 15.dp, bottom = 12.dp),
                                text = it.title,
                                style = MaterialTheme.typography.headlineLarge,
                            )
                            HorizontalDivider(modifier = Modifier.align(Alignment.BottomStart))
                        }
                    }
                }
            }

            RoundButton(
                modifier = Modifier.fillMaxWidth(),
                text = "추가하기",
                color = MaterialTheme.colorScheme.primary,
                onClick = {
                    onClick(selectSearchPolicy)
                    onDismiss()
                },
            )
        }
    }
}
