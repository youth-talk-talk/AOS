package com.example.policydetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youth.app.feature.policydetail.R
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray50

@Composable
fun CommentTextField(modifier: Modifier = Modifier) {
    val focusManager = LocalFocusManager.current

    var text by remember {
        mutableStateOf("")
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 18.dp),
        contentAlignment = Alignment.Center,
    ) {
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 7.dp)
                .background(
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    shape = RoundedCornerShape(8.dp),
                ),
            value = text,
            textStyle = MaterialTheme.typography.titleMedium,
            onValueChange = { text = it },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                },
            ),
        ) { innerTextField ->

            Box(
                modifier = Modifier
                    .padding(horizontal = 13.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                innerTextField()
                if (text.isEmpty()) {
                    Text(
                        text = "댓글을 달아보세요",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = gray50,
                        ),
                    )
                }

                Icon(
                    modifier = Modifier
                        .padding(vertical = 13.dp)
                        .align(Alignment.CenterEnd),
                    painter = painterResource(id = R.drawable.arrow_up_icon),
                    contentDescription = "댓글 등록",
                    tint = Color.Black,
                )
            }
        }
    }
}

@Preview
@Composable
private fun CommentScreenPreview() {
    YongProjectTheme {
        CommentTextField()
    }
}
