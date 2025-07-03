package com.core.community.screen.detail

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.core.community.component.ReportDialog
import com.core.community.model.ReportType
import com.core.community.model.ReportType.Post
import com.core.community.model.detail.CommunityDetailType
import com.core.community.model.detail.CommunityDetailUiEffect
import com.core.community.model.detail.CommunityDetailUiEvent
import com.core.community.model.detail.CommunityDetailUiState
import com.core.community.viewmodel.CommunityDetailViewModel
import com.youth.app.feature.community.R
import com.youthtalk.component.comment.UserComment
import com.youthtalk.component.dialog.ModalDialog
import com.youthtalk.component.dialog.UserBlockDialog
import com.youthtalk.component.empty.EmptyScreen
import com.youthtalk.component.screen.CommentModifyScreen
import com.youthtalk.component.topbar.MiddleTitleTopBar
import com.youthtalk.designsystem.YongProjectTheme
import com.youthtalk.designsystem.gray100
import com.youthtalk.designsystem.gray30
import com.youthtalk.designsystem.gray40
import com.youthtalk.designsystem.gray70
import com.youthtalk.designsystem.gray80
import com.youthtalk.designsystem.gray90
import com.youthtalk.model.comment.Comment
import com.youthtalk.model.post.PostDetail
import com.youthtalk.model.post.PostSubject
import com.youthtalk.util.getTime
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun CommunityDetailScreen(
    viewModel: CommunityDetailViewModel = hiltViewModel(),
    showSnackBar: (String) -> Unit,
    onBack: () -> Unit,
    onModifyWriteCommunity: (PostSubject, Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    var comments by rememberSaveable {
        mutableStateOf(Pair(0L, ""))
    }
    var deletePostDialog by remember {
        mutableStateOf(Pair<Boolean, Long?>(false, null))
    }

    var reportDialog by remember {
        mutableStateOf(Pair<Boolean, ReportType>(false, Post))
    }

    var blockUserDialog by remember { mutableStateOf(Triple<Boolean, Long, String>(false, 0L, "")) }

    BackHandler {
        when (state.detailType) {
            CommunityDetailType.MAIN -> onBack()
            CommunityDetailType.COMMENT -> viewModel.setEvent(CommunityDetailUiEvent.ChangeDetailType(CommunityDetailType.MAIN))
        }
    }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest {
            when (it) {
                is CommunityDetailUiEffect.ShowSnackBarDeleteComment -> {
                    showSnackBar("댓글이 성공적으로 삭제됐습니다.")
                }

                is CommunityDetailUiEffect.ShowSnackBarModifyComment -> {
                    showSnackBar("댓글이 변경됐습니다.")
                }

                is CommunityDetailUiEffect.ShowSnackBarDeletePost -> {
                    onBack()
                    showSnackBar("게시글이 성공적으로 삭제됐습니다.")
                }

                is CommunityDetailUiEffect.ShowSnackBarReportPost -> {
                    onBack()
                    showSnackBar(context.getString(R.string.report_post_success))
                }

                is CommunityDetailUiEffect.ShowSnackBarReportComment -> {
                    showSnackBar(context.getString(R.string.report_comment_success))
                }

                is CommunityDetailUiEffect.ShowSnackBarReportFail -> {
                    showSnackBar(it.message.toString())
                }
                is CommunityDetailUiEffect.InitError -> {
                    showSnackBar(it.message)
                    onBack()
                }
                is CommunityDetailUiEffect.ShowSnackBarBlockUser -> {
                    showSnackBar(context.getString(R.string.block_user_snackbar_message, it.userName))
                    onBack()
                }
            }
        }
    }

    if (!state.initLoading) {
        Crossfade(
            modifier = modifier,
            targetState = state.detailType
        ) {
            when (it) {
                CommunityDetailType.MAIN -> {
                    DetailScreen(
                        state = state,
                        onBack = onBack,
                        onPostModifyComment = { comment ->
                            comments = Pair(comment.commentId, comment.content)
                            viewModel.setEvent(CommunityDetailUiEvent.ChangeDetailType(CommunityDetailType.COMMENT))
                        },
                        onPostAddComment = { message ->
                            viewModel.setEvent(CommunityDetailUiEvent.PostAddComment(state.postDetail.postId, message))
                        },
                        onDeleteComment = { comment ->
                            viewModel.setEvent(CommunityDetailUiEvent.PostDeleteComment(comment))
                        },
                        onPostDeletePost = { postId -> deletePostDialog = Pair(true, postId) },
                        onPostModifyPost = { postId ->
                            val postType = if (state.postDetail.postType == "post") PostSubject.POST else PostSubject.REVIEW
                            onModifyWriteCommunity(postType, postId)
                        },
                        onPostReportPost = {
                            reportDialog = Pair(true, Post)
                        },
                        onPostReportPostUser = { userId, userName ->
                            blockUserDialog = Triple(true, userId, userName)
                        },
                        onReportComment = { commentId ->
                            reportDialog = Pair(true, ReportType.Comment(commentId))
                        },
                        onPostPostScrap = { postId, scrap -> viewModel.setEvent(CommunityDetailUiEvent.PostPostScrap(postId, scrap)) },
                        onCommentLike = { commentId, scrap -> viewModel.setEvent(CommunityDetailUiEvent.PostCommentLike(commentId, scrap)) }
                    )
                }

                CommunityDetailType.COMMENT -> CommentModifyScreen(
                    comment = comments.second,
                    onBack = { viewModel.setEvent(CommunityDetailUiEvent.ChangeDetailType(CommunityDetailType.MAIN)) },
                    onPostCommentModify = { viewModel.setEvent(CommunityDetailUiEvent.PatchModifyComment(comments.first, comments.second)) },
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

    if (deletePostDialog.first) {
        deletePostDialog.second?.let { postId ->
            ModalDialog(
                title = "게시글을 삭제할까요?",
                subTitle = "게시글을 삭제하면 모든 데이터가 삭제되고 다시 볼 수 없습니다.",
                confirmBackground = MaterialTheme.colorScheme.error,
                confirmText = "삭제하기",
                onDismissRequest = { deletePostDialog = Pair(false, null) },
                onClickConfirm = { viewModel.setEvent(CommunityDetailUiEvent.DeletePost(postId)) }
            )
        }
    }

    if (reportDialog.first) {
        val reportType = reportDialog.second
        val onClickConfirm: () -> Unit = {
            when (reportType) {
                Post -> {
                    viewModel.setEvent(CommunityDetailUiEvent.ReportPost(viewModel.state.value.postDetail.postId))
                }
                is ReportType.Comment -> {
                    viewModel.setEvent(CommunityDetailUiEvent.ReportComment(reportType.commentId))
                }
            }
        }
        ReportDialog(
            reportType = reportType,
            onClickConfirm = onClickConfirm,
            onCloseDialog = {
                reportDialog = Pair(false, Post)
            }
        )
    }

    if (blockUserDialog.first) {
        UserBlockDialog(
            userName = blockUserDialog.third,
            onDismissRequest = { blockUserDialog = blockUserDialog.copy(first = false) },
            onClickConfirm = {
                viewModel.setEvent(CommunityDetailUiEvent.BlockUser(userId = blockUserDialog.second, userName = blockUserDialog.third))
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DetailScreen(
    state: CommunityDetailUiState,
    onBack: () -> Unit,
    onPostAddComment: (String) -> Unit,
    onPostModifyComment: (Comment) -> Unit,
    onDeleteComment: (Comment) -> Unit,
    onPostModifyPost: (Long) -> Unit,
    onPostDeletePost: (Long) -> Unit,
    onPostReportPost: () -> Unit,
    onPostReportPostUser: (userId: Long, userName: String) -> Unit,
    onReportComment: (commentId: Long) -> Unit,
    onPostPostScrap: (Long, Boolean) -> Unit,
    onCommentLike: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    var bottomSheet by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    Column(
        modifier = modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus()
            }
    ) {
        MiddleTitleTopBar(
            onBack = onBack,
            tails = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Image(
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                onPostPostScrap(state.postDetail.postId, state.postDetail.scrap)
                            },
                        painter = painterResource(if (state.postDetail.scrap) R.drawable.bookmark_fill else R.drawable.bookmark_line),
                        contentDescription = "스크랩",
                        colorFilter = ColorFilter.tint(color = if (state.postDetail.scrap) MaterialTheme.colorScheme.primary else gray100)
                    )

                    Image(
                        modifier = Modifier
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                bottomSheet = true
                            },
                        painter = painterResource(R.drawable.more),
                        contentDescription = "더보기",
                        colorFilter = ColorFilter.tint(color = gray100)
                    )
                }
            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            item {
                PostDetailContent(postDetail = state.postDetail)
            }

            item {
                Text(
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 20.dp,
                        top = 16.dp
                    ),
                    text = "댓글 ${state.comments.commentCount}",
                    style = MaterialTheme.typography.displayLarge
                )
            }

            if (state.comments.commentCount != 0) {
                items(
                    count = state.comments.commentCount
                ) {
                    UserComment(
                        comment = state.comments.comments[it],
                        isMine = state.user.memberId == state.comments.comments[it].writerId,
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                        onDeleteComment = onDeleteComment,
                        onPostReportComment = onReportComment,
                        onPostModifyComment = onPostModifyComment,
                        onPostReportUser = onPostReportPostUser,
                        onCommentLike = onCommentLike
                    )
                }
            } else {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        EmptyScreen(
                            modifier = Modifier
                                .fillMaxWidth(),
                            emptyTitle = "아직 댓글이 없어요.\n가장 먼저 댓글을 남겨보세요."
                        )
                    }
                }
            }
        }

        ChatTextField(
            onPostAddComment = onPostAddComment
        )
    }

    if (bottomSheet) {
        ModalBottomSheet(
            sheetState = bottomSheetState,
            onDismissRequest = { bottomSheet = false },
            containerColor = Color(0xFDFFFFFF),
            contentColor = Color(0xFDFFFFFF)
        ) {
            val isMine = state.postDetail.writerId == state.user.memberId
            val list = if (isMine) listOf("수정하기", "삭제하기") else listOf("게시글 신고하기", "사용자 차단하기")
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 20.dp)
            ) {
                list.forEachIndexed { index, text ->
                    Text(
                        modifier = Modifier
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                scope.launch {
                                    bottomSheetState.hide()
                                    bottomSheet = false
                                }
                                if (index == 0) {
                                    if (isMine) onPostModifyPost(state.postDetail.postId) else onPostReportPost()
                                } else {
                                    if (isMine) {
                                        onPostDeletePost(
                                            state.postDetail.postId
                                        )
                                    } else {
                                        onPostReportPostUser(state.user.memberId, state.user.nickname)
                                    }
                                }
                            }
                            .padding(vertical = 14.dp),
                        text = text,
                        style = MaterialTheme.typography.displaySmall
                    )
                }
                HorizontalDivider(
                    color = gray40
                )
                Text(
                    modifier = Modifier
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            scope.launch {
                                bottomSheetState.hide()
                                bottomSheet = false
                            }
                        }
                        .padding(vertical = 14.dp),
                    text = "취소하기",
                    style = MaterialTheme.typography.displaySmall
                )
            }
        }
    }
}

@Composable
private fun PostDetailContent(modifier: Modifier = Modifier, postDetail: PostDetail) {
    val postDate by remember {
        mutableStateOf(postDetail.updatedAt.getTime())
    }
    Row(
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        postDetail.profileImage?.let { image ->
            AsyncImage(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape),
                model = image,
                contentDescription = "기본 이미지"
            )
        } ?: Image(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape),
            painter = painterResource(R.drawable.profile_thumnail),
            contentDescription = "기본 이미지"
        )
        val nickname = when (postDetail.nickname) {
            "null" -> "탈퇴한 회원"
            else -> postDetail.nickname
        } ?: "탈퇴한 회원"

        Column {
            Text(
                text = nickname,
                style = MaterialTheme.typography.displayLarge
            )
            Text(
                text = postDate,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = gray80
                )
            )
        }
    }
    HorizontalDivider(
        color = gray40
    )

    Column(
        modifier = Modifier
            .heightIn(min = 300.dp)
            .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 30.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = postDetail.title,
            style = MaterialTheme.typography.bodyMedium
        )

        postDetail.contentList.forEach { content ->
            if (content.type == "TEXT") {
                Text(
                    text = content.content,
                    style = MaterialTheme.typography.displaySmall
                )
            } else {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize(),
                    model = content.content,
                    contentDescription = null
                )
            }
        }
    }

    HorizontalDivider(
        thickness = 10.dp,
        color = gray30
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatTextField(modifier: Modifier = Modifier, onPostAddComment: (String) -> Unit) {
    var textValue by rememberSaveable {
        mutableStateOf("")
    }
    val focusManager = LocalFocusManager.current
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val focusRequester = remember { FocusRequester() }
    val scope = rememberCoroutineScope()

    Row(
        modifier = modifier
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
            textStyle = MaterialTheme.typography.titleSmall,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Default
            ),
            keyboardActions = KeyboardActions(
                onDone = {}
            )
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
                    onPostAddComment(textValue)
                    textValue = ""
                    focusManager.clearFocus()
                },
            painter = painterResource(R.drawable.send),
            contentDescription = "보내기",
            colorFilter = ColorFilter.tint(color = if (textValue.isEmpty()) gray70 else gray90)
        )
    }
}

@Preview
@Composable
private fun CommunityDetailPreview() {
    YongProjectTheme {
        CommunityDetailScreen(
            showSnackBar = {},
            onBack = {},
            onModifyWriteCommunity = { _, _ -> }
        )
    }
}
