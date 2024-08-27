package com.youthtalk.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.youth.app.core.designsystem.R
import com.youthtalk.util.clickableSingle

@Composable
fun CustomDialog(title: String, onCancel: () -> Unit, onSuccess: () -> Unit, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = { onDismiss() },
    ) {
        Column(
            modifier = Modifier
                .height(240.dp)
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(20.dp),
                )
                .padding(horizontal = 24.dp, vertical = 17.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            Image(
                modifier = Modifier.padding(top = 12.dp),
                painter = painterResource(id = R.drawable.info_circle_icon),
                contentDescription = "경고",
            )
            Box(
                modifier = Modifier.weight(1f)
                    .padding(vertical = 20.dp),
            ) {
                Text(
                    modifier = Modifier.align(Alignment.CenterStart),
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.W400,
                    ),
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(13.dp),
            ) {
                RoundButton(
                    modifier = Modifier
                        .weight(1f)
                        .clickableSingle { onCancel() },
                    text = "아니요",
                    color = MaterialTheme.colorScheme.onSurface,
                ) {
                }
                RoundButton(
                    modifier = Modifier
                        .weight(1f)
                        .clickableSingle { onSuccess() },
                    text = "예",
                    color = MaterialTheme.colorScheme.primary,
                ) {
                }
            }
        }
    }
}
