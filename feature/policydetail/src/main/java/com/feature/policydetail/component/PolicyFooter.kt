package com.feature.policydetail.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.youthtalk.component.comment.UserComment

fun LazyListScope.policyFooter() {
    items(
        count = 10
    ) {
        if (it == 0) {
            Text(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 20.dp,
                    top = 16.dp
                ),
                text = "댓글 7",
                style = MaterialTheme.typography.displayLarge
            )
        }

        UserComment(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        )
    }
}
