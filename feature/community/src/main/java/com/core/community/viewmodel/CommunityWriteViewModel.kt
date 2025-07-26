package com.core.community.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.core.base.BaseViewModel
import com.core.community.model.Contents
import com.core.community.model.write.CommunityWriteUiEffect
import com.core.community.model.write.CommunityWriteUiEvent
import com.core.community.model.write.CommunityWriteUiState
import com.core.domain.usercase.GetImageListUseCase
import com.core.domain.usercase.policy.PostSearchPolicyUseCase
import com.core.domain.usercase.post.GetPostDetailUseCase
import com.core.domain.usercase.post.PostCreatePostUseCase
import com.core.domain.usercase.post.PostModifyPostUseCase
import com.youthtalk.model.post.CreatePost
import com.youthtalk.model.post.ModifyPost
import com.youthtalk.model.post.PostContent
import com.youthtalk.model.post.PostSubject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class CommunityWriteViewModel @Inject constructor(
    private val getImageListUseCase: GetImageListUseCase,
    private val getPostDetailUseCase: GetPostDetailUseCase,
    private val postCreatePostUseCase: PostCreatePostUseCase,
    private val postModifyPostUseCase: PostModifyPostUseCase,
    private val postSearchPolicyUseCase: PostSearchPolicyUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<CommunityWriteUiState, CommunityWriteUiEvent, CommunityWriteUiEffect>(
    initialState = CommunityWriteUiState.initState
) {

    val permissions = mutableStateListOf<String>()

    init {
        val communityType = savedStateHandle.get<PostSubject>("communityType") ?: PostSubject.REVIEW
        val postId = savedStateHandle.get<Long>("postId")
        setEvent(CommunityWriteUiEvent.InitData(postId, communityType))
    }

    override fun handleEvents(event: CommunityWriteUiEvent) {
        when (event) {
            is CommunityWriteUiEvent.InitData -> initData(event.postId, event.postSubject)
            is CommunityWriteUiEvent.OnTextChangeValue -> textChangeValue(event.index, event.text)
            is CommunityWriteUiEvent.GetImages -> getImages()
            is CommunityWriteUiEvent.FocusChange -> focusChange(event.index, event.text)
            is CommunityWriteUiEvent.ChangeTitle -> setTitle(event.title)
            is CommunityWriteUiEvent.SearchPolicyChangeTextValue -> setState { copy(searchPolicy = event.searchPolicy) }
            is CommunityWriteUiEvent.PostSearchPolicy -> postSearchPolicy(event.searchPolicy)
            is CommunityWriteUiEvent.ClearSearchInfo -> {
                setState { copy(searchPolicies = emptyFlow(), searchPolicy = "") }
                setEffect { CommunityWriteUiEffect.OnBack }
            }

            is CommunityWriteUiEvent.OnClickSearchPolicy -> {
                setState {
                    copy(
                        policyId = event.search.policyId,
                        policyName = event.search.policyTitle,
                        searchPolicies = emptyFlow(),
                        searchPolicy = ""
                    )
                }
                setEffect { CommunityWriteUiEffect.OnBack }
            }

            is CommunityWriteUiEvent.PostCreatePost -> state.value.postId?.let { postModify(it) } ?: postCreatePost()
            is CommunityWriteUiEvent.ImageDelete -> deleteImage(event.index)
        }
    }

    private fun postModify(postId: Long) {
        val modifyPost = ModifyPost(
            title = state.value.title,
            postType = state.value.postType.name.lowercase(),
            policyId = state.value.policyId?.toString(),
            contentList = state.value.contentList.map {
                when (it) {
                    is Contents.Text -> {
                        PostContent(content = it.textFieldValue.text, type = "TEXT")
                    }

                    is Contents.Image -> {
                        PostContent(content = it.imgUrl, type = "IMAGE")
                    }
                }
            },
            addImgUrlList = state.value.addImgUrlList,
            deletedImgUrlList = state.value.deletedImgUrlList
        )

        viewModelScope.launch {
            postModifyPostUseCase(postId, modifyPost)
                .onSuccess { postId ->
                    setEffect { CommunityWriteUiEffect.Modify(postId) }
                }
        }
    }

    private fun initData(postId: Long?, postType: PostSubject) {
        postId?.let { id ->
            viewModelScope.launch {
                getPostDetailUseCase(id)
                    .onSuccess { postDetail ->
                        setState {
                            copy(
                                postId = id,
                                title = postDetail.title,
                                postType = PostSubject.entries.find { type -> type.name.lowercase() == postDetail.postType } ?: postType,
                                policyId = postDetail.policyId,
                                policyName = postDetail.policyTitle,
                                contentList = postDetail.contentList.map { contents ->
                                    when (contents.type) {
                                        "TEXT" -> {
                                            Contents.Text(TextFieldValue(text = contents.content, selection = TextRange(contents.content.length)))
                                        }

                                        else -> {
                                            Contents.Image(imgUrl = contents.content)
                                        }
                                    }
                                }
                            )
                        }
                    }
            }
        } ?: setState { copy(postType = postType) }
    }

    private fun deleteImage(index: Int) {
        val image = state.value.contentList[index]
        if (image !is Contents.Image) return

        val content = state.value.contentList[index + 1]
        if (content !is Contents.Text) return
        val contents = state.value.contentList.filterIndexed { i, _ -> !((i == index) || (i == index + 1)) }.toMutableList()
        val prevText = contents[index - 1]
        if (prevText is Contents.Text) {
            val newText = prevText.textFieldValue.text + content.textFieldValue.text
            contents[index - 1] = Contents.Text(
                textFieldValue = TextFieldValue(text = newText, selection = TextRange(newText.length))
            )

            setState {
                copy(
                    contentList = contents,
                    deletedImgUrlList = if (deletedImgUrlList.contains(image.imgUrl)) deletedImgUrlList else deletedImgUrlList + image.imgUrl,
                    focusIndex = Pair(index - 1, TextFieldValue(text = newText, selection = TextRange(newText.length)))
                )
            }
        }
    }

    private fun postCreatePost() {
        val createPost = CreatePost(
            title = state.value.title,
            postType = state.value.postType.name.lowercase(),
            policyId = state.value.policyId?.toString(),
            contentList = state.value.contentList.map {
                when (it) {
                    is Contents.Text -> {
                        PostContent(content = it.textFieldValue.text, type = "TEXT")
                    }

                    is Contents.Image -> {
                        PostContent(content = it.imgUrl, type = "IMAGE")
                    }
                }
            }
        )
        viewModelScope.launch {
            postCreatePostUseCase(createPost)
                .onSuccess {
                    setEffect { CommunityWriteUiEffect.CreatePost }
                }
        }
    }

    private fun postSearchPolicy(searchPolicy: String) {
        viewModelScope.launch {
            val searchPolicies = postSearchPolicyUseCase(searchPolicy)
                .catch {
                    Timber.e("CommunityWriteViewModel postSearchPolicy error $it")
                }

            setState { copy(searchPolicies = searchPolicies.cachedIn(viewModelScope)) }
        }
    }

    private fun textChangeValue(index: Int, text: TextFieldValue) {
        val contents = state.value.contentList.toMutableList()
        val content = contents[index]
        if (content !is Contents.Text) return

        contents.set(index = index, content.copy(textFieldValue = text))
        setState { copy(contentList = contents, focusIndex = Pair(index, text)) }
    }

    private fun focusChange(index: Int, text: TextFieldValue?) {
        setState { copy(focusIndex = Pair(index, text)) }
    }

    private fun getImages() {
        viewModelScope.launch {
            getImageListUseCase()
                .onSuccess {
                    setState { copy(images = it) }
                    setEffect { CommunityWriteUiEffect.GoPictureScreen(it) }
                }
        }
    }

    private fun setTitle(title: String) {
        setState { copy(title = title) }
    }

    fun dismissDialog() {
        permissions.removeAll(permissions)
    }

    fun onPermissionResult(permission: String, isGranted: Boolean) {
        if (!isGranted && !permissions.contains(permission)) {
            permissions.add(permission)
        }
    }
}
