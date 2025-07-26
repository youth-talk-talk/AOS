package com.core.mypage.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.core.base.BaseViewModel
import com.core.domain.usercase.GetUserUseCase
import com.core.domain.usercase.comment.GetSettingCommentUseCase
import com.core.domain.usercase.comment.PatchCommentUseCase
import com.core.domain.usercase.comment.PostCommentLikeUseCase
import com.core.domain.usercase.comment.PostDeleteCommentUseCase
import com.core.mypage.model.comment.CommentUiEffect
import com.core.mypage.model.comment.CommentUiEvent
import com.core.mypage.model.comment.CommentUiState
import com.core.mypage.model.comment.SettingCommentScreenType
import com.core.navigation.model.CommentType
import com.youthtalk.model.comment.ArticleType
import com.youthtalk.model.comment.SettingComment
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val getSettingCommentUseCase: GetSettingCommentUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val postDeleteCommentUseCase: PostDeleteCommentUseCase,
    private val patchCommentUseCase: PatchCommentUseCase,
    private val postCommentLikeUseCase: PostCommentLikeUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<CommentUiState, CommentUiEvent, CommentUiEffect>(
    initialState = CommentUiState.initState
) {

    init {
        savedStateHandle.get<CommentType>("type")?.let {
            setEvent(CommentUiEvent.InitData(it))
        }
    }

    override fun handleEvents(event: CommentUiEvent) {
        when (event) {
            is CommentUiEvent.InitData -> initData(event.type)
            is CommentUiEvent.OnClickModify -> onClickModify(event.comment)
            is CommentUiEvent.OnDeleteComment -> onDeleteComment(event.comment)
            is CommentUiEvent.OnBackMain -> {
                setEffect { CommentUiEffect.ModifyInfo(0, "") }
                setState { copy(commentScreenType = SettingCommentScreenType.MAIN) }
            }

            is CommentUiEvent.OnPatchComment -> onPatchComment(event.commentId, event.content)
            is CommentUiEvent.PostCommentLike -> postCommentLike(event.commentId, event.isLike)
            is CommentUiEvent.RefreshData -> refreshData()
            is CommentUiEvent.OnClickCard -> {
                when (event.type) {
                    ArticleType.POLICY -> setEffect { CommentUiEffect.DetailPolicy(event.id) }
                    else -> setEffect { CommentUiEffect.DetailPost(event.id) }
                }
            }
        }
    }

    private fun refreshData() {
        viewModelScope.launch {
            getSettingCommentUseCase(state.value.commentType == CommentType.LIKE)
                .catch {
                    Timber.e("CommentViewModel refreshData error $it")
                }
                .collectLatest { commentInfo ->
                    setState { copy(commentInfo = commentInfo) }
                }
        }
    }

    private fun postCommentLike(commentId: Long, isLike: Boolean) {
        viewModelScope.launch {
            postCommentLikeUseCase(commentId, isLike)
                .onSuccess {
                    setState {
                        copy(
                            commentInfo = commentInfo.copy(
                                comments = commentInfo.comments.map { info ->
                                    if (info.commentId != commentId) {
                                        info
                                    } else {
                                        info.copy(
                                            isLikedByMember = !isLike,
                                            likeCount = info.likeCount + (if (isLike) -1 else +1)
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

    private fun onPatchComment(commentId: Long, content: String) {
        viewModelScope.launch {
            patchCommentUseCase(commentId, content)
                .onSuccess {
                    setState {
                        copy(
                            commentScreenType = SettingCommentScreenType.MAIN,
                            commentInfo = commentInfo.copy(
                                comments = commentInfo.comments.map { info ->
                                    if (info.commentId != commentId) {
                                        info
                                    } else {
                                        info.copy(content = content)
                                    }
                                }
                            )
                        )
                    }
                    setEffect { CommentUiEffect.ModifyInfo(0, "") }
                }.onFailure {
                    Timber.e("error $it")
                }
        }
    }

    private fun onDeleteComment(comment: SettingComment) {
        viewModelScope.launch {
            postDeleteCommentUseCase(comment.commentId)
                .onSuccess {
                    setState {
                        copy(
                            commentInfo = commentInfo.copy(
                                commentCount = commentInfo.commentCount - 1,
                                comments = commentInfo.comments.filter { info -> info.commentId != comment.commentId }
                            )
                        )
                    }
                    setEffect { CommentUiEffect.ShowSnackBar(it) }
                }
        }
    }

    private fun onClickModify(comment: SettingComment) {
        setEffect { CommentUiEffect.ModifyInfo(comment.commentId, comment.content) }
        setState { copy(commentScreenType = SettingCommentScreenType.COMMENT) }
    }

    private fun initData(type: CommentType) {
        viewModelScope.launch {
            combine(
                getSettingCommentUseCase(type == CommentType.LIKE),
                getUserUseCase()
            ) { commentInfo, user ->
                Pair(commentInfo, user)
            }
                .catch {
                    Timber.e("CommentViewModel initData error $it")
                }
                .collectLatest { (commentInfo, user) ->
                    setState { copy(isLoading = false, commentInfo = commentInfo, commentType = type, user = user) }
                }
        }
    }
}
