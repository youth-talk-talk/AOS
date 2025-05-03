package com.feature.policydetail.screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.feature.policydetail.component.header
import com.feature.policydetail.component.policyContent
import com.feature.policydetail.component.policyFooter
import com.youth.app.feature.policydetail.R
import com.youthtalk.component.topbar.MiddleTitleTopBar
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray100
import com.youthtalk.designsystem.gray30
import com.youthtalk.designsystem.gray70
import com.youthtalk.designsystem.gray90
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PolicyDetailScreen(modifier: Modifier = Modifier) {
    var textValue by remember {
        mutableStateOf("")
    }
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val focusRequester = remember { FocusRequester() }
    val scope = rememberCoroutineScope()
    val ime = LocalSoftwareKeyboardController.current
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val density = LocalDensity.current
    var isExpanded by remember {
        mutableStateOf(false)
    }
    var isPolicySummaryExpanded by remember {
        mutableStateOf(true)
    }
    var contentHeight by remember {
        mutableStateOf(0.dp)
    }
    var measuredOnce by remember { mutableStateOf(false) }
    val animatedHeight by animateDpAsState(
        targetValue = if (!isExpanded && contentHeight > screenHeight * 0.4f) screenHeight * 0.4f else contentHeight,
        label = "expandHeight",
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    ime?.hide()
                }
            },
    ) {
        MiddleTitleTopBar(
            onBack = {},
            tails = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Image(
                        painter = painterResource(R.drawable.share),
                        contentDescription = "공유하기",
                        colorFilter = ColorFilter.tint(color = gray100),
                    )

                    Image(
                        painter = painterResource(R.drawable.bookmark_line),
                        contentDescription = "공유하기",
                        colorFilter = ColorFilter.tint(color = gray100),
                    )
                }
            },
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            header(
                isExpand = isPolicySummaryExpanded,
                onClickExpand = { isPolicySummaryExpanded = !isPolicySummaryExpanded },
            )
            policyContent(
                screenHeight = screenHeight,
                animatedHeight = animatedHeight,
                measuredOnce = measuredOnce,
                contentHeight = contentHeight,
                isExpanded = isExpanded,
                measureHeight = { height ->
                    if (!measuredOnce) {
                        contentHeight = with(density) {
                            height.toDp()
                        }
                        measuredOnce = true
                    }
                    Timber.e("contentHeight = $contentHeight")
                },
                onClickExpanded = {
                    isExpanded = !isExpanded
                },
            )
            policyFooter()
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .background(
                    color = gray30,
                    shape = RoundedCornerShape(6.dp),
                )
                .heightIn(max = 80.dp)
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.Top,
        ) {
            BasicTextField(
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester)
                    .bringIntoViewRequester(bringIntoViewRequester)
                    .onFocusChanged {
                        if (it.isFocused) {
                            scope.launch {
                                bringIntoViewRequester.bringIntoView()
                            }
                        }
                    },
                value = textValue,
                onValueChange = { textValue = it },
                textStyle = MaterialTheme.typography.titleSmall,
            ) { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    if (textValue.isEmpty()) {
                        Text(
                            text = "댓글을 입력해 보세요!",
                            style = MaterialTheme.typography.titleSmall.copy(
                                color = gray70,
                            ),
                        )
                    }
                    innerTextField()
                }
            }

            Image(
                modifier = Modifier.size(18.dp),
                painter = painterResource(R.drawable.send),
                contentDescription = "보내기",
                colorFilter = ColorFilter.tint(color = if (textValue.isEmpty()) gray70 else gray90),
            )
        }
    }
}

fun Modifier.isMeasure(measure: Boolean, animationHeight: Dp): Modifier {
    return if (measure) {
        this.height(animationHeight)
    } else {
        this
    }
}

private fun shared(context: Context, text: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        this.setType("text/plain")
        putExtra(Intent.EXTRA_TEXT, text)
    }
    val chooser = Intent.createChooser(intent, "공유하기")
    context.startActivity(chooser)
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(modifier: Modifier = Modifier, url: String) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient() // 페이지 로딩을 WebView 안에서 처리
                settings.javaScriptEnabled = true // JavaScript 사용 가능하게 설정
                loadUrl(url)
            }
        },
        update = { webView ->
            webView.loadUrl(url)
        },
    )
}

@Preview
@Composable
private fun PolicyDetailScreenPreview() {
    YongProjectTheme {
        PolicyDetailScreen()
    }
}
