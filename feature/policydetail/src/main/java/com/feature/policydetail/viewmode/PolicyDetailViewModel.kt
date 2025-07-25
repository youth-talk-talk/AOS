package com.feature.policydetail.viewmode

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.core.base.BaseViewModel
import com.core.domain.usercase.GetUserUseCase
import com.core.domain.usercase.PostPolicyAddCommentUseCase
import com.core.domain.usercase.PostPolicyScrapUseCase
import com.core.domain.usercase.comment.PatchCommentUseCase
import com.core.domain.usercase.comment.PostCommentLikeUseCase
import com.core.domain.usercase.comment.PostDeleteCommentUseCase
import com.core.domain.usercase.policydetail.GetPolicyDetailCommentUseCase
import com.core.domain.usercase.policydetail.GetPolicyDetailUseCase
import com.feature.policydetail.model.PolicyDetailType
import com.feature.policydetail.model.PolicyDetailUiEffect
import com.feature.policydetail.model.PolicyDetailUiEvent
import com.feature.policydetail.model.PolicyDetailUiState
import com.youthtalk.model.comment.Comment
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

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
    savedStateHandle: SavedStateHandle
) : BaseViewModel<PolicyDetailUiState, PolicyDetailUiEvent, PolicyDetailUiEffect>(
    initialState = PolicyDetailUiState.initState
) {

    init {
        savedStateHandle.get<Long>("policyId")?.let { policyId ->
            setEvent(PolicyDetailUiEvent.InitData(policyId))
        }
    }

    override fun handleEvents(event: PolicyDetailUiEvent) {
        when (event) {
            is PolicyDetailUiEvent.InitData -> initData(event.policyId)
            is PolicyDetailUiEvent.LinkUrl -> linkUrl(event.url)
            is PolicyDetailUiEvent.Shared -> setEffect {
                PolicyDetailUiEffect.Share(
                    if (event.url.matches("^http(s)?://.+".toRegex())) {
                        event.url
                    } else {
                        "https://${event.url}"
                    }
                )
            }

            is PolicyDetailUiEvent.ChangeDetailType -> {
                setEffect { PolicyDetailUiEffect.ChangeComment(event.commentId, event.message) }
                setState { copy(detailType = event.type) }
            }

            is PolicyDetailUiEvent.PatchModifyComment -> patchModifyComment(event.commentId, event.message)
            is PolicyDetailUiEvent.PostAddPolicyComment -> postAddPolicyComment(event.policyId, event.message)
            is PolicyDetailUiEvent.PostDeleteComment -> postDeleteComment(event.comment)
            is PolicyDetailUiEvent.PolicyScrap -> postPolicyScrap(event.policyId, event.scrap)
            is PolicyDetailUiEvent.PostCommentLike -> postCommentLike(event.commentId, event.isLike)
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

    private fun postPolicyScrap(policyId: Long, scrap: Boolean) {
        viewModelScope.launch {
            postPolicyScrapUseCase(policyId, scrap)
                .catch {
                    Timber.e("PolicyDetailViewModel postPolicyScrap error $it")
                }
                .collectLatest {
                    setState { copy(policyDetail = state.value.policyDetail.copy(isScrap = !scrap)) }
                }
        }
    }

    private fun patchModifyComment(commentId: Long, message: String) {
        viewModelScope.launch {
            patchCommentUseCase(commentId, message)
                .onSuccess {
                    val newComments = state.value.commentInfo.comments
                        .map { comment -> if (comment.commentId == commentId) comment.copy(content = message) else comment }
                    setState {
                        copy(
                            commentInfo = state.value.commentInfo.copy(
                                comments = newComments
                            )
                        )
                    }
                    setEvent(PolicyDetailUiEvent.ChangeDetailType(PolicyDetailType.MAIN))
                    setEffect { PolicyDetailUiEffect.ShowSnackBarModifyComment }
                }.onFailure {
                    Timber.e("error : $it")
                }
        }
    }

    private fun postDeleteComment(comment: Comment) {
        viewModelScope.launch {
            postDeleteCommentUseCase(comment.commentId)
                .catch {
                    Timber.e("PolicyDetailViewModel postDeleteComment error $it")
                }
                .collectLatest {
                    val newComments = state.value.commentInfo.comments.toMutableList()
                    newComments.remove(comment)
                    setState {
                        copy(
                            commentInfo = state.value.commentInfo.copy(
                                commentCount = state.value.commentInfo.commentCount - 1,
                                comments = newComments
                            )
                        )
                    }
                    setEffect { PolicyDetailUiEffect.ShowSnackBarDeleteComment }
                }
        }
    }

    private fun postAddPolicyComment(policyId: Long, message: String) {
        viewModelScope.launch {
            postPolicyAddCommentUseCase(policyId, message)
                .catch {
                    Timber.e("PolicyDetailViewModel postAddPolicyComment error $it")
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
                            commentInfo = state.value.commentInfo.copy(
                                commentCount = state.value.commentInfo.commentCount + 1,
                                comments = state.value.commentInfo.comments + newComment
                            )
                        )
                    }
                }
        }
    }

    private fun linkUrl(url: String) {
        setEffect {
            PolicyDetailUiEffect.LinkBrowser(
                if (url.matches("^http(s)?://.+".toRegex())) {
                    url
                } else {
                    "https://$url"
                }
            )
        }
    }

    private fun initData(policyId: Long) {
        viewModelScope.launch {
            val commentInfo = getPolicyDetailCommentUseCase(policyId)
            val policyDetail = getPolicyDetailUseCase(policyId)

            getUserUseCase()
                .catch {
                    Timber.e("error $it")
                }.collectLatest { user ->
                    PolicyDetailUiState(
                        isLoading = false,
                        user = user,
                        policyDetail = policyDetail.getOrThrow(),
                        commentInfo = commentInfo.getOrThrow(),
                        policyId = policyId
                    )
                }
        }
    }
}
