package com.core.community.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.core.community.model.ReportType
import com.core.community.model.ReportType.Comment
import com.core.community.model.ReportType.Post
import com.youth.app.feature.community.R
import com.youthtalk.component.dialog.ModalDialog
import com.youthtalk.designsystem.YongProjectTheme

@Composable
internal fun ReportDialog(reportType: ReportType, onClickConfirm: () -> Unit, onCloseDialog: () -> Unit, modifier: Modifier = Modifier) {
    val title = when (reportType) {
        Post -> stringResource(R.string.report_post_title)
        Comment -> stringResource(R.string.report_comment_title)
    }

    ModalDialog(
        title = title,
        confirmText = stringResource(R.string.report_confirm),
        confirmBackground = MaterialTheme.colorScheme.error,
        onClickConfirm = onClickConfirm,
        onDismissRequest = onCloseDialog,
        onClickCancel = onCloseDialog
    )
}

@Preview
@Composable
private fun ReportDialogPreview() {
    YongProjectTheme {
        ReportDialog(
            reportType = Post,
            onClickConfirm = {},
            onCloseDialog = {}
        )
    }
}
