package com.youthtalk.component.dialog

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.youth.app.core.designsystem.R
import com.youthtalk.designsystem.YongProjectTheme

@Composable
fun UserBlockDialog(userName: String, onDismissRequest: () -> Unit, onClickConfirm: () -> Unit, modifier: Modifier = Modifier) {
    ModalDialog(
        title = stringResource(R.string.block_user_title),
        subTitle = stringResource(R.string.block_user_subtitle, userName),
        confirmText = stringResource(R.string.block),
        confirmBackground = MaterialTheme.colorScheme.error,
        onDismissRequest = onDismissRequest,
        onClickConfirm = onClickConfirm,
        modifier = modifier
    )
}

@Preview
@Composable
private fun UserBlockDialogPreview() {
    YongProjectTheme {
        UserBlockDialog(
            userName = "똑똑한 청년",
            onDismissRequest = {},
            onClickConfirm = {},
            modifier = Modifier
        )
    }
}
