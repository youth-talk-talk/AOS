package com.core.community.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.core.base.BaseViewModel
import com.core.community.model.detail.CommunityDetailType
import com.core.community.model.detail.CommunityDetailUiEffect
import com.core.community.model.detail.CommunityDetailUiEvent
import com.core.community.model.detail.CommunityDetailUiState
import com.core.domain.usercase.GetUserUseCase
import com.core.domain.usercase.comment.PatchCommentUseCase
import com.core.domain.usercase.comment.PostAddPostCommentUseCase
import com.core.domain.usercase.comment.PostCommentLikeUseCase
import com.core.domain.usercase.comment.PostDeleteCommentUseCase
import com.core.domain.usercase.post.DeletePostUseCase
import com.core.domain.usercase.post.GetPostDetailCommentsUseCase
import com.core.domain.usercase.post.GetPostDetailUseCase
import com.core.domain.usercase.post.PostPostScrapUseCase
import com.core.domain.usercase.report.ReportCommentUseCase
import com.core.domain.usercase.report.ReportPostUseCase
import com.core.domain.usercase.user.BlockUserUseCase
import com.youthtalk.model.comment.Comment
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class CommunityDetailViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getPostDetailUseCase: GetPostDetailUseCase,
    private val getPostDetailCommentsUseCase: GetPostDetailCommentsUseCase,
    private val postAddPostCommentUseCase: PostAddPostCommentUseCase,
    private val postDeleteCommentUseCase: PostDeleteCommentUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val patchCommentUseCase: PatchCommentUseCase,
    private val postPostScrapUseCase: PostPostScrapUseCase,
    private val postCommentLikeUseCase: PostCommentLikeUseCase,
    private val reportPostUseCase: ReportPostUseCase,
    private val reportCommentUseCase: ReportCommentUseCase,
    private val blockUserUseCase: BlockUserUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<CommunityDetailUiState, CommunityDetailUiEvent, CommunityDetailUiEffect>(
    initialState = CommunityDetailUiState.initState
) {
    init {
        savedStateHandle.get<Long>("postId")?.let { postId ->
            setEvent(CommunityDetailUiEvent.InitData(postId))
        }
    }

    override fun handleEvents(event: CommunityDetailUiEvent) {
        when (event) {
            is CommunityDetailUiEvent.InitData -> initData(event.postId)
            is CommunityDetailUiEvent.ChangeDetailType -> setState { copy(detailType = event.detailType) }
            is CommunityDetailUiEvent.PostAddComment -> postAddPostComment(event.postId, event.message)
            is CommunityDetailUiEvent.PostDeleteComment -> postDeleteComment(event.comment)
            is CommunityDetailUiEvent.PatchModifyComment -> patchModifyComment(event.commentId, event.message)
            is CommunityDetailUiEvent.DeletePost -> deletePost(event.postId)
            is CommunityDetailUiEvent.PostPostScrap -> postPostScrap(event.postId, event.scrap)
            is CommunityDetailUiEvent.PostCommentLike -> postCommentLike(event.commentId, event.isLike)
            is CommunityDetailUiEvent.ReportPost -> reportPost(event.postId)
            is CommunityDetailUiEvent.ReportComment -> reportComment(event.commentId)
            is CommunityDetailUiEvent.BlockUser -> blockUser(event.userId, event.userName)
        }
    }

    private fun postCommentLike(commentId: Long, isLike: Boolean) {
        viewModelScope.launch {
            postCommentLikeUseCase(commentId, isLike)
                .onSuccess {
                    setState {
                        copy(
                            comments = comments.copy(
                                comments = comments.comments.map { info ->
                                    if (info.commentId != commentId) {
                                        info
                                    } else {
                                        info.copy(
                                            isLikedByMember = !isLike
                                        )
                                    }
                                }
                            )
                        )
                    }
                }.onFailure {
                    Timber.e("error $it")
                }
        }
    }

    private fun postPostScrap(postId: Long, scrap: Boolean) {
        viewModelScope.launch {
            postPostScrapUseCase(postId, scrap)
                .catch {
                    Timber.e("CommunityDetailViewModel postPostScrap error $it")
                }
                .collectLatest {
                    setState {
                        copy(
                            postDetail = state.value.postDetail.copy(scrap = !scrap)
                        )
                    }
                }
        }
    }

    private fun deletePost(postId: Long) {
        viewModelScope.launch {
            deletePostUseCase(postId)
                .catch {
                    Timber.e("CommunityDetailViewModel deletePost error $it")
                }
                .collectLatest {
                    setEffect { CommunityDetailUiEffect.ShowSnackBarDeletePost }
                }
        }
    }

    private fun patchModifyComment(commentId: Long, message: String) {
        viewModelScope.launch {
            patchCommentUseCase(commentId, message)
                .onSuccess {
                    val newComments = state.value.comments.comments
                        .map { comment -> if (comment.commentId == commentId) comment.copy(content = message) else comment }
                    setState {
                        copy(
                            comments = state.value.comments.copy(
                                comments = newComments
                            ),
                            detailType = CommunityDetailType.MAIN
                        )
                    }
                    setEffect { CommunityDetailUiEffect.ShowSnackBarModifyComment }
                }.onFailure {
                    Timber.e("error : $it")
                }
        }
    }

    private fun postDeleteComment(comment: Comment) {
        viewModelScope.launch {
            postDeleteCommentUseCase(comment.commentId)
                .catch {
                    Timber.e("CommunityDetailViewModel postDeleteComment error $it")
                }
                .collectLatest {
                    val newComments = state.value.comments.comments.toMutableList()
                    newComments.remove(comment)
                    setState {
                        copy(
                            comments = state.value.comments.copy(
                                commentCount = state.value.comments.commentCount - 1,
                                comments = newComments
                            )
                        )
                    }
                    setEffect { CommunityDetailUiEffect.ShowSnackBarDeleteComment }
                }
        }
    }

    private fun postAddPostComment(postId: Long, message: String) {
        viewModelScope.launch {
            postAddPostCommentUseCase(postId, message)
                .onSuccess {
                    val newComment = Comment(
                        commentId = it,
                        writerId = state.value.user.memberId,
                        nickname = state.value.user.nickname,
                        content = message,
                        isLikedByMember = false,
                        profileImg = state.value.user.profileImgUrl,
                        createdAt = LocalDateTime.now()
                    )

                    setState {
                        copy(
                            comments = state.value.comments.copy(
                                commentCount = state.value.comments.commentCount + 1,
                                comments = state.value.comments.comments + newComment
                            )
                        )
                    }
                }.onFailure {
                    Timber.e("error : $it")
                }
        }
    }

    private fun reportPost(postId: Long) {
        viewModelScope.launch {
            reportPostUseCase(postId)
                .onSuccess {
                    setEffect { CommunityDetailUiEffect.ShowSnackBarReportPost }
                }.onFailure {
                    setEffect { CommunityDetailUiEffect.ShowSnackBarReportFail(it.message) }
                }
        }
    }

    private fun reportComment(commentId: Long) {
        viewModelScope.launch {
            reportCommentUseCase(commentId)
                .onSuccess {
                    setEffect { CommunityDetailUiEffect.ShowSnackBarReportComment }
                }.onFailure {
                    setEffect { CommunityDetailUiEffect.ShowSnackBarReportFail(it.message) }
                }
        }
    }

    private fun blockUser(userId: Long, userName: String) {
        viewModelScope.launch {
            blockUserUseCase(userId)
                .onSuccess {
                    setEffect { CommunityDetailUiEffect.ShowSnackBarBlockUser(userName) }
                }.onFailure {
                }
        }
    }

    private fun initData(postId: Long) {
        viewModelScope.launch {
            val commentInfo = getPostDetailCommentsUseCase(postId)
            val postDetail = getPostDetailUseCase(postId)

            getUserUseCase()
                .catch {
                    setEffect { CommunityDetailUiEffect.InitError("신고한 게시글은 조회할 수 없습니다.") }
                }.collect { user ->
                    CommunityDetailUiState(
                        user = user,
                        postDetail = postDetail.getOrThrow(),
                        comments = commentInfo.getOrThrow(),
                        detailType = CommunityDetailType.MAIN,
                        initLoading = false
                    )
                }
        }
    }
}
