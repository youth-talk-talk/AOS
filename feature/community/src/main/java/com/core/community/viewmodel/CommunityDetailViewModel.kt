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
import com.core.domain.usercase.comment.PostDeleteCommentUseCase
import com.core.domain.usercase.post.DeletePostUseCase
import com.core.domain.usercase.post.GetPostDetailCommentsUseCase
import com.core.domain.usercase.post.GetPostDetailUseCase
import com.core.domain.usercase.post.PostPostScrapUseCase
import com.youthtalk.model.comment.Comment
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
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
                .catch {
                    Timber.e("CommunityDetailViewModel patchModifyComment error $it")
                }
                .collectLatest {
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
                .catch {
                    Timber.e("CommunityDetailViewModel postAddPostComment error $it")
                }
                .collectLatest {
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
                }
        }
    }

    private fun initData(postId: Long) {
        viewModelScope.launch {
            combine(
                getUserUseCase(),
                getPostDetailUseCase(postId),
                getPostDetailCommentsUseCase(postId)
            ) { user, postDetail, comments ->
                CommunityDetailUiState(
                    user = user,
                    postDetail = postDetail,
                    comments = comments,
                    detailType = CommunityDetailType.MAIN,
                    initLoading = false
                )
            }
                .catch {
                    Timber.e("CommunityDetailViewModel initData error $it")
                }
                .collectLatest { uiState ->
                    setState { uiState }
                }
        }
    }
}
