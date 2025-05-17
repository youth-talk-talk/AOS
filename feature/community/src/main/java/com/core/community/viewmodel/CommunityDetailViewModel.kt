package com.core.community.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.core.base.BaseViewModel
import com.core.community.model.detail.CommunityDetailType
import com.core.community.model.detail.CommunityDetailUiEffect
import com.core.community.model.detail.CommunityDetailUiEvent
import com.core.community.model.detail.CommunityDetailUiState
import com.core.domain.usercase.GetUserUseCase
import com.core.domain.usercase.comment.PostAddPostCommentUseCase
import com.core.domain.usercase.post.GetPostDetailCommentsUseCase
import com.core.domain.usercase.post.GetPostDetailUseCase
import com.youthtalk.model.Comment
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
            is CommunityDetailUiEvent.OnPostAddComment -> postAddPostComment(event.postId, event.message)
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
