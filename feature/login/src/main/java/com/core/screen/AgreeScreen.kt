package com.core.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youth.app.feature.login.R
import com.youthtalk.component.button.CheckButton
import com.youthtalk.component.checkbox.CustomCheckBox
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray100
import com.youthtalk.designsystem.gray20
import com.youthtalk.designsystem.gray30
import com.youthtalk.designsystem.gray80
import com.youthtalk.designsystem.gray90

@Composable
fun AgreeScreen(clickNext: () -> Unit, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(horizontal = 16.dp)
    ) {
        var isExpand by remember {
            mutableStateOf(false)
        }

        var isCheck by rememberSaveable {
            mutableStateOf(false)
        }

        LoginAppBar(onClickBack = onBack)
        Text(
            text = stringResource(R.string.term_title),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = gray100
            )
        )

        TermCheckBox(
            isCheck = isCheck,
            isExpand = isExpand,
            onClickCheck = { isCheck = !isCheck },
            onClickExpand = { isExpand = !isExpand }
        )

        LawInfo(isExpand = isExpand)
        Spacer(Modifier.weight(1f))

        CheckButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 4.dp,
                    vertical = 26.dp
                ),
            isCheck = isCheck,
            text = stringResource(R.string.next)
        ) { clickNext() }
    }
}

@Composable
internal fun LoginAppBar(onClickBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 20.dp)
    ) {
        Image(
            modifier = Modifier.clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClickBack()
            },
            painter = painterResource(R.drawable.arrowleft),
            contentDescription = stringResource(R.string.back)
        )
    }
}

@Composable
private fun TermCheckBox(isCheck: Boolean, isExpand: Boolean, onClickCheck: () -> Unit, onClickExpand: () -> Unit) {
    val expandIcon = if (isExpand) R.drawable.arrowup else R.drawable.arrowdown
    Row(
        modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClickCheck()
            }
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomCheckBox(
            isCheck = isCheck,
            onClick = onClickCheck
        )
        Text(
            modifier = Modifier.padding(start = 8.dp, end = 4.dp),
            text = stringResource(R.string.checkbox_title),
            style = MaterialTheme.typography.displayMedium.copy(
                color = gray80
            )
        )
        Text(
            text = stringResource(R.string.checkbox_required),
            style = MaterialTheme.typography.displayMedium.copy(
                color = MaterialTheme.colorScheme.primary
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        Image(
            modifier = Modifier.clickable {
                onClickExpand()
            },
            painter = painterResource(expandIcon),
            contentDescription = stringResource(R.string.expand)
        )
    }
}

@Composable
fun LawInfo(isExpand: Boolean) {
    AnimatedVisibility(
        modifier = Modifier.fillMaxWidth(),
        visible = isExpand
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(360.dp)
                .padding(top = 6.dp)
                .background(
                    color = gray20,
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    width = 1.dp,
                    color = gray30,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.law_info),
                style = MaterialTheme.typography.labelSmall.copy(
                    color = gray90
                )
            )
        }
    }
}

@Preview
@Composable
private fun AgreeScreenPreview() {
    YongProjectTheme {
        AgreeScreen(
            clickNext = {},
            onBack = {}
        )
    }
}
