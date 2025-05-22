package com.feature.policydetail.component

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.feature.policydetail.screen.isMeasure
import com.youthtalk.designsystem.gray30
import com.youthtalk.designsystem.gray40
import com.youthtalk.designsystem.gray90
import com.youthtalk.model.policy.PolicyDetail
import timber.log.Timber

fun LazyListScope.policyContent(
    policyDetail: PolicyDetail,
    screenHeight: Dp,
    animatedHeight: Dp,
    contentHeight: Dp,
    isExpanded: Boolean,
    measuredOnce: Boolean,
    measureHeight: (Int) -> Unit,
    onClickExpanded: () -> Unit,
    onClickLink: (String) -> Unit
) {
    item {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .isMeasure(measuredOnce, animatedHeight)
                    .onGloballyPositioned { coordinates ->
                        measureHeight(coordinates.size.height)
                    }
                    .padding(start = 16.dp, end = 16.dp),
                verticalArrangement = Arrangement.spacedBy(30.dp)
            ) {
                if (policyDetail.applyQualifications) {
                    PolicySpecContent(
                        title = "신청자격",
                        contents = policyDetail.getApplyQualifications()
                    )
                }

                policyDetail.supportDetail?.let { support ->
                    PolicySpecContent(
                        title = "지원내용",
                        contents = listOf(support)
                    )
                }

                if (policyDetail.applyMethod) {
                    PolicySpecContent(
                        title = "신청방법",
                        contents = policyDetail.getApplyMethod(),
                        onClickLink = onClickLink
                    )
                }
            }

            if (!isExpanded && contentHeight > screenHeight * 0.4f) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .height(20.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.White),
                                startY = 0f,
                                endY = with(LocalDensity.current) { 20.dp.toPx() }
                            )
                        )
                )
            }
        }

        if (contentHeight > screenHeight * 0.4f) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 20.dp, top = 20.dp)
                    .border(
                        width = 1.dp,
                        color = gray40,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onClickExpanded()
                    }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isExpanded) "접기" else "공고 상세보기",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(top = 10.dp, bottom = 20.dp),
            thickness = 10.dp,
            color = gray30
        )
    }
}

@Composable
fun PolicySpecContent(modifier: Modifier = Modifier, title: String, contents: List<String>, onClickLink: (String) -> Unit = {}) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge
        )

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            contents.forEach {
                if (Patterns.WEB_URL.matcher(it).matches()) {
                    val annotatedText = buildAnnotatedString {
                        pushStringAnnotation(
                            tag = "URL",
                            annotation = it
                        )
                        withStyle(style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
                            append(it)
                        }
                        pop()
                    }

                    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }
                    val gesture = Modifier.pointerInput(Unit) {
                        detectTapGestures { pos ->
                            layoutResult.value?.let { layoutResult ->
                                val offset = layoutResult.getOffsetForPosition(pos)
                                annotatedText
                                    .getStringAnnotations(tag = "URL", start = offset, end = offset)
                                    .firstOrNull()?.item?.let { url ->
                                        Timber.e("url : $url")
                                        onClickLink(url)
                                    }
                            }
                        }
                    }

                    Text(
                        text = annotatedText,
                        modifier = modifier.then(gesture),
                        style = MaterialTheme.typography.displaySmall,
                        onTextLayout = { layout ->
                            layoutResult.value = layout
                        }
                    )
                } else {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.displaySmall.copy(
                            color = gray90
                        )
                    )
                }
            }
        }
    }
}
