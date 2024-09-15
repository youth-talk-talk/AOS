package com.example.policydetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.domain.usercase.GetUserUseCase
import com.core.domain.usercase.PostCommentLikeUseCase
import com.core.domain.usercase.PostDeleteCommentUseCase
import com.core.domain.usercase.PostPolicyAddCommentUseCase
import com.core.domain.usercase.PostPolicyScrapUseCase
import com.core.domain.usercase.policydetail.GetPolicyDetailCommentUseCase
import com.core.domain.usercase.policydetail.GetPolicyDetailUseCase
import com.core.domain.usercase.post.PatchCommentUseCase
import com.example.policydetail.model.PolicyDetailUiState
import com.youthtalk.model.Comment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PolicyDetailViewModel @Inject constructor(
    private val getPolicyDetailUseCase: GetPolicyDetailUseCase,
    private val getPolicyDetailCommentUseCase: GetPolicyDetailCommentUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val postPolicyScrapUseCase: PostPolicyScrapUseCase,
    private val postPolicyAddCommentUseCase: PostPolicyAddCommentUseCase,
    private val postDeleteCommentUseCase: PostDeleteCommentUseCase,
    private val patchCommentUseCase: PatchCommentUseCase,
    private val postCommentLikeUseCase: PostCommentLikeUseCase,
) : ViewModel() {

    private val _error = MutableSharedFlow<Throwable>()
    val error = _error.asSharedFlow()

    private val _uiState = MutableStateFlow<PolicyDetailUiState>(PolicyDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun getData(policyId: String) {
        viewModelScope.launch {
            combine(
                getPolicyDetailUseCase(policyId),
                getPolicyDetailCommentUseCase(policyId),
                getUserUseCase(),
            ) { policyDetail, comments, user ->
                PolicyDetailUiState.Success(
                    policyDetail = policyDetail,
                    myInfo = user,
                    comments = comments.toPersistentList(),
                )
            }
                .catch {
                    Log.d("YOON-CHAN", "PolicyDetailViewModel getData error ${it.message}")
                    _error.emit(it)
                }
                .collectLatest { state ->
                    _uiState.value = state
                }
        }
    }

    fun postScrap(id: String, scrap: Boolean) {
        val state = _uiState.value
        if (state !is PolicyDetailUiState.Success) return

        viewModelScope.launch {
            postPolicyScrapUseCase(id, scrap)
                .catch {
                    Log.d("YOON-CHAN", "PolicyDetailViewModel postScrap error ${it.message}")
                }
                .collectLatest {
                    _uiState.value = state.copy(
                        policyDetail = state.policyDetail.copy(
                            isScrap = !state.policyDetail.isScrap,
                        ),
                    )
                }
        }
    }

    fun addComment(policyId: String, text: String) {
        val state = _uiState.value
        if (state !is PolicyDetailUiState.Success) return

        viewModelScope.launch {
            postPolicyAddCommentUseCase(policyId, text)
                .catch {
                    Log.d("YOON-CHAN", "PolicyDetailViewModel addComment error ${it.message}")
                }
                .collectLatest {
                    val newComment = Comment(
                        commentId = it,
                        nickname = state.myInfo.nickname,
                        content = text,
                        isLikedByMember = false,
                    )
                    val comment = state.comments.toMutableList()
                    comment.add(0, newComment)
                    _uiState.value = state.copy(
                        comments = comment.toPersistentList(),
                    )
                }
        }
    }

    fun deleteComment(index: Int, commentId: Long) {
        val state = _uiState.value
        if (state !is PolicyDetailUiState.Success) return

        viewModelScope.launch {
            postDeleteCommentUseCase(commentId)
                .catch {
                    Log.d("YOON-CHAN", "PolicyDetailViewModel deleteComment error ${it.message}")
                }
                .collectLatest {
                    val comments = state.comments.toMutableList()
                    comments.removeAt(index)
                    _uiState.value = state.copy(
                        comments = comments.toPersistentList(),
                    )
                }
        }
    }

    fun modifyComment(id: Long, content: String) {
        val state = _uiState.value
        if (state !is PolicyDetailUiState.Success) return

        viewModelScope.launch {
            patchCommentUseCase(id, content)
                .catch {
                    Log.d("YOON-CHAN", "PolicyDetailViewModel modifyComment error ${it.message}")
                }
                .collectLatest {
                    _uiState.value = state.copy(
                        comments = state.comments.map { if (it.commentId == id) it.copy(content = content) else it }.toPersistentList(),
                    )
                }
        }
    }

    fun postCommentLike(id: Long, isLike: Boolean) {
        val state = _uiState.value
        if (state !is PolicyDetailUiState.Success) return

        viewModelScope.launch {
            postCommentLikeUseCase(id, !isLike)
                .catch {
                    Log.d("YOON-CHAN", "PolicyDetailViewModel postCommentLike error ${it.message}")
                }
                .collectLatest {
                    _uiState.value = state.copy(
                        comments = state.comments.map { if (it.commentId == id) it.copy(isLikedByMember = !isLike) else it }.toPersistentList(),
                    )
                }
        }
    }
}
