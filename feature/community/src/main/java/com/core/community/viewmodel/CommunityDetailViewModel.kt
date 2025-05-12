package com.core.community.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.community.model.CommunityDetailUiEffect
import com.core.community.model.CommunityDetailUiEvent
import com.core.community.model.CommunityDetailUiState
import com.core.domain.usercase.GetUserUseCase
import com.core.domain.usercase.PostCommentLikeUseCase
import com.core.domain.usercase.PostDeleteCommentUseCase
import com.core.domain.usercase.PostPostScrapUseCase
import com.core.domain.usercase.post.DeletePostUseCase
import com.core.domain.usercase.post.GetPostDetailCommentUseCase
import com.core.domain.usercase.post.GetPostDetailUseCase
import com.core.domain.usercase.post.PatchCommentUseCase
import com.core.domain.usercase.post.PostPostAddCommentUseCase
import com.core.exception.BadRequestException
import com.youthtalk.model.Comment
import com.youthtalk.model.PostType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class CommunityDetailViewModel @Inject constructor(
    private val getPostDetailUseCase: GetPostDetailUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val getPostDetailCommentUseCase: GetPostDetailCommentUseCase,
    private val postCommentLikeUseCase: PostCommentLikeUseCase,
    private val postPostAddCommentUseCase: PostPostAddCommentUseCase,
    private val postDeleteCommentUseCase: PostDeleteCommentUseCase,
    private val patchCommentUseCase: PatchCommentUseCase,
    private val postPostScrapUseCase: PostPostScrapUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    init {
        val postId = savedStateHandle.get<Long>("postId") ?: -1
        uiEvent(CommunityDetailUiEvent.GetPostDetail(postId))
    }

    private val _uiState = MutableStateFlow<CommunityDetailUiState>(CommunityDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    var uiEffect = MutableSharedFlow<CommunityDetailUiEffect>()
        private set

    fun uiEvent(event: CommunityDetailUiEvent) {
        when (event) {
            is CommunityDetailUiEvent.GetPostDetail -> getPostDetail(event.id)
            is CommunityDetailUiEvent.PostCommentLike -> postCommentIsLike(event.id, event.isLike)
            is CommunityDetailUiEvent.PostAddComment -> postAddComment(event.id, event.text)
            is CommunityDetailUiEvent.DeleteComment -> deleteComment(event.index, event.commentId)
            is CommunityDetailUiEvent.ModifyComment -> modifyComment(event.id, event.content)
            is CommunityDetailUiEvent.PostScrap -> postPostScrap(event.postId, event.isScrap, event.type)
            is CommunityDetailUiEvent.ModifyPost -> goWriteScreen()
            is CommunityDetailUiEvent.DeletePost -> deletePost(event.postId)
        }
    }

    private fun goWriteScreen() {
        val state = _uiState.value
        if (state !is CommunityDetailUiState.Success) return

        viewModelScope.launch {
            uiEffect.emit(
                CommunityDetailUiEffect.CommunityWrite(
                    state.post.postId,
                    state.post.postType
                )
            )
        }
    }

    private fun postPostScrap(postId: Long, isScrap: Boolean, type: PostType) {
        val state = _uiState.value
        if (state !is CommunityDetailUiState.Success) return

        viewModelScope.launch {
            postPostScrapUseCase(postId, isScrap, type)
                .catch {
                    Timber.e("CommunityDetailViewModel modifyComment error " + it.message)
                }
                .collectLatest {
                    _uiState.value = state.copy(
                        post = state.post.copy(scrap = !isScrap)
                    )
                }
        }
    }

    private fun modifyComment(id: Long, content: String) {
        val state = _uiState.value
        if (state !is CommunityDetailUiState.Success) return

        viewModelScope.launch {
            patchCommentUseCase(id, content)
                .catch {
                    Timber.e("CommunityDetailViewModel modifyComment error " + it.message)
                }
                .collectLatest {
                    _uiState.value = state.copy(
                        comments = state.comments.map { if (id == it.commentId) it.copy(content = content) else it }.toPersistentList()
                    )
                }
        }
    }

    private fun postAddComment(id: Long, text: String) {
        val state = _uiState.value
        if (state !is CommunityDetailUiState.Success) return

        viewModelScope.launch {
            postPostAddCommentUseCase(id, text)
                .catch {
                    Timber.e("CommunityDetailViewModel postAddComment error " + it.message)
                }
                .collectLatest {
                    val newComment = Comment(
                        commentId = it,
                        nickname = state.user.nickname,
                        content = text,
                        isLikedByMember = false
                    )
                    val comment = state.comments.toMutableList()
                    comment.add(newComment)
                    _uiState.value = state.copy(comments = comment.toPersistentList())
                }
        }
    }

    private fun postCommentIsLike(id: Long, isLike: Boolean) {
        val state = _uiState.value
        if (state !is CommunityDetailUiState.Success) return

        viewModelScope.launch {
            postCommentLikeUseCase(id, !isLike)
                .catch {
                    Timber.e("CommunityDetailViewModel postCommentIsLike error " + it.message)
                }
                .collectLatest {
                    _uiState.value = state.copy(
                        comments = state.comments.map { if (it.commentId == id) it.copy(isLikedByMember = !isLike) else it }.toPersistentList()
                    )
                }
        }
    }

    private fun getPostDetail(id: Long) {
        viewModelScope.launch {
            combine(
                getPostDetailUseCase(id),
                getUserUseCase(),
                getPostDetailCommentUseCase(id)
            ) { post, user, comments ->
                CommunityDetailUiState.Success(
                    post = post,
                    user = user,
                    comments = comments.toPersistentList()
                )
            }
                .catch {
                    Timber.e("CommunityDetailViewModel getPostDetail error " + it.message)
                    when (it) {
                        is BadRequestException -> uiEffect.emit(CommunityDetailUiEffect.NotFoundPost(true, it.message))
                    }
                }
                .collectLatest {
                    _uiState.value = it
                }
        }
    }

    private fun deleteComment(index: Int, commentId: Long) {
        val state = _uiState.value
        if (state !is CommunityDetailUiState.Success) return

        viewModelScope.launch {
            postDeleteCommentUseCase(commentId)
                .catch {
                    Timber.e("PolicyDetailViewModel deleteComment error " + it.message)
                }
                .collectLatest {
                    val comments = state.comments.toMutableList()
                    comments.removeAt(index)
                    _uiState.value = state.copy(
                        comments = comments.toPersistentList()
                    )
                }
        }
    }

    private fun deletePost(postId: Long) {
        val state = _uiState.value
        if (state !is CommunityDetailUiState.Success) return

        viewModelScope.launch {
            deletePostUseCase(postId)
                .catch {
                    Timber.e("PolicyDetailViewModel deletePost error ${it.message}")
                }
                .collectLatest {
                    Timber.e("PolicyDetailViewModel deletePost Success $it")
                    uiEffect.emit(CommunityDetailUiEffect.PostDelete(true))
                }
        }
    }
}
