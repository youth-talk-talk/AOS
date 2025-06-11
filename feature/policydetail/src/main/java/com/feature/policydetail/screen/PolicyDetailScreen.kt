package com.feature.policydetail.screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.feature.policydetail.component.header
import com.feature.policydetail.component.policyContent
import com.feature.policydetail.component.policyFooter
import com.feature.policydetail.model.PolicyDetailType
import com.feature.policydetail.model.PolicyDetailUiEffect
import com.feature.policydetail.model.PolicyDetailUiEvent
import com.feature.policydetail.model.PolicyDetailUiState
import com.feature.policydetail.viewmode.PolicyDetailViewModel
import com.youth.app.feature.policydetail.R
import com.youthtalk.component.screen.CommentModifyScreen
import com.youthtalk.component.topbar.MiddleTitleTopBar
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray10
import com.youthtalk.designsystem.gray100
import com.youthtalk.designsystem.gray30
import com.youthtalk.designsystem.gray70
import com.youthtalk.designsystem.gray90
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun PolicyDetailScreenRoot(
    viewModel: PolicyDetailViewModel = hiltViewModel(),
    onBack: () -> Unit,
    showSnackBar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val lazyListState = rememberLazyListState()
    var comments by rememberSaveable {
        mutableStateOf(Pair(0L, ""))
    }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest {
            when (it) {
                is PolicyDetailUiEffect.LinkBrowser -> {
                    Timber.e("LinkBrowser ${it.url}")
                    val intent = Intent(Intent.ACTION_VIEW, it.url.toUri())
                    context.startActivity(intent)
                }

                is PolicyDetailUiEffect.Share -> {
                    shared(context, it.url)
                }

                is PolicyDetailUiEffect.ShowSnackBarDeleteComment -> {
                    showSnackBar("댓글이 성공적으로 삭제됐습니다.")
                }

                is PolicyDetailUiEffect.ShowSnackBarModifyComment -> {
                    showSnackBar("댓글이 변경됐습니다.")
                }

                is PolicyDetailUiEffect.ChangeComment -> {
                    comments = Pair(it.commentId, it.message)
                }
            }
        }
    }

    BackHandler {
        when (state.detailType) {
            PolicyDetailType.MAIN -> onBack()
            PolicyDetailType.COMMENT -> viewModel.setEvent(PolicyDetailUiEvent.ChangeDetailType(PolicyDetailType.MAIN))
        }
    }

    if (!state.isLoading) {
        Crossfade(
            modifier = modifier,
            targetState = state.detailType
        ) {
            when (it) {
                PolicyDetailType.MAIN -> {
                    PolicyDetailScreen(
                        state = state,
                        lazyListState = lazyListState,
                        onBack = onBack,
                        actionEvent = viewModel::setEvent
                    )
                }

                PolicyDetailType.COMMENT -> CommentModifyScreen(
                    comment = comments.second,
                    onBack = { viewModel.setEvent(PolicyDetailUiEvent.ChangeDetailType(PolicyDetailType.MAIN)) },
                    onPostCommentModify = { viewModel.setEvent(PolicyDetailUiEvent.PatchModifyComment(comments.first, comments.second)) },
                    onTextChange = { text -> comments = comments.copy(second = text) }
                )
            }
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun PolicyDetailScreen(
    lazyListState: LazyListState,
    state: PolicyDetailUiState,
    onBack: () -> Unit,
    actionEvent: (PolicyDetailUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    var textValue by remember {
        mutableStateOf("")
    }
    val focusManager = LocalFocusManager.current
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val focusRequester = remember { FocusRequester() }
    val scope = rememberCoroutineScope()
    val ime = LocalSoftwareKeyboardController.current
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val density = LocalDensity.current
    var isExpanded by remember { mutableStateOf(false) }
    var isPolicySummaryExpanded by remember { mutableStateOf(true) }
    var contentHeight by remember { mutableStateOf(0.dp) }
    var measuredOnce by remember { mutableStateOf(false) }
    val animatedHeight by animateDpAsState(
        targetValue = if (!isExpanded && contentHeight > screenHeight * 0.4f) screenHeight * 0.4f else contentHeight,
        label = "expandHeight"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = gray10)
            .pointerInput(Unit) {
                detectTapGestures {
                    ime?.hide()
                }
            }
    ) {
        MiddleTitleTopBar(
            onBack = onBack,
            tails = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    state.policyDetail.applUrl?.let { url ->
                        Image(
                            modifier = Modifier
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    actionEvent(PolicyDetailUiEvent.Shared(url))
                                },
                            painter = painterResource(R.drawable.share),
                            contentDescription = "공유하기",
                            colorFilter = ColorFilter.tint(color = gray100)
                        )
                    }

                    Image(
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                actionEvent(PolicyDetailUiEvent.PolicyScrap(state.policyId, state.policyDetail.isScrap))
                            },
                        painter = painterResource(if (state.policyDetail.isScrap) R.drawable.bookmark_fill else R.drawable.bookmark_line),
                        contentDescription = "스크랩",
                        colorFilter = ColorFilter.tint(color = if (state.policyDetail.isScrap) MaterialTheme.colorScheme.primary else gray100)
                    )
                }
            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            state = lazyListState
        ) {
            header(
                isExpand = isPolicySummaryExpanded,
                policyDetail = state.policyDetail,
                onClickExpand = { isPolicySummaryExpanded = !isPolicySummaryExpanded },
                onClickLink = { url -> actionEvent(PolicyDetailUiEvent.LinkUrl(url)) }
            )
            policyContent(
                policyDetail = state.policyDetail,
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
                onClickLink = { url -> actionEvent(PolicyDetailUiEvent.LinkUrl(url)) }
            )
            policyFooter(
                commentInfo = state.commentInfo,
                user = state.user,
                onPostModifyComment = { comment ->
                    actionEvent(PolicyDetailUiEvent.ChangeDetailType(PolicyDetailType.COMMENT, comment.commentId, comment.content))
                },
                onDeleteComment = { actionEvent(PolicyDetailUiEvent.PostDeleteComment(it)) },
                onCommentLike = { id, scrap -> actionEvent(PolicyDetailUiEvent.PostCommentLike(id, scrap)) }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .background(
                    color = gray30,
                    shape = RoundedCornerShape(6.dp)
                )
                .heightIn(max = 80.dp)
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.Top
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
                textStyle = MaterialTheme.typography.titleSmall
            ) { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    if (textValue.isEmpty()) {
                        Text(
                            text = "댓글을 입력해 보세요!",
                            style = MaterialTheme.typography.titleSmall.copy(
                                color = gray70
                            )
                        )
                    }
                    innerTextField()
                }
            }

            Image(
                modifier = Modifier
                    .size(18.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        if (textValue.isNotEmpty()) {
                            actionEvent(PolicyDetailUiEvent.PostAddPolicyComment(policyId = state.policyId, message = textValue))
                            textValue = ""
                            focusManager.clearFocus()
                        }
                    },
                painter = painterResource(R.drawable.send),
                contentDescription = "보내기",
                colorFilter = ColorFilter.tint(color = if (textValue.isEmpty()) gray70 else gray90)
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
        }
    )
}

@Preview
@Composable
private fun PolicyDetailScreenPreview() {
    YongProjectTheme {
        val lazyListState = rememberLazyListState()
        PolicyDetailScreen(
            state = PolicyDetailUiState.initState,
            lazyListState = lazyListState,
            onBack = {},
            actionEvent = {}
        )
    }
}
