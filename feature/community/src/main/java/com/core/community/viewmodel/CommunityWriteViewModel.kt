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
import com.core.domain.usercase.PostUploadImageUseCase
import com.core.domain.usercase.policy.PostSearchPolicyUseCase
import com.core.domain.usercase.post.PostCreatePostUseCase
import com.youthtalk.model.post.CreatePost
import com.youthtalk.model.post.PostContent
import com.youthtalk.model.post.PostSubject
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class CommunityWriteViewModel @Inject constructor(
    private val getImageListUseCase: GetImageListUseCase,
    private val postUploadImageUseCase: PostUploadImageUseCase,
    private val postCreatePostUseCase: PostCreatePostUseCase,
    private val postSearchPolicyUseCase: PostSearchPolicyUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<CommunityWriteUiState, CommunityWriteUiEvent, CommunityWriteUiEffect>(
    initialState = CommunityWriteUiState.initState
) {

    val permissions = mutableStateListOf<String>()

    init {
        val communityType = savedStateHandle.get<PostSubject>("communityType") ?: PostSubject.REVIEW
        setState { copy(postType = communityType) }
    }

    override fun handleEvents(event: CommunityWriteUiEvent) {
        when (event) {
            is CommunityWriteUiEvent.OnTextChangeValue -> textChangeValue(event.index, event.text)
            is CommunityWriteUiEvent.GetImages -> getImages()
            is CommunityWriteUiEvent.PostUploadImages -> uploadImage(event.file)
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

            is CommunityWriteUiEvent.PostCreatePost -> postCreatePost()
            is CommunityWriteUiEvent.ImageDelete -> deleteImage(event.index)
        }
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
        }
        setState {
            copy(
                contentList = contents
            )
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
                .catch {
                    Timber.e("CommunityWriteViewModel postCreatePost error $it")
                }
                .collectLatest {
                    Timber.e("CommunityWriteViewModel postCreatePost success $it")
                    setEffect { CommunityWriteUiEffect.CreatePost }
                }
        }
    }

    private fun postSearchPolicy(searchPolicy: String) {
        viewModelScope.launch {
            postSearchPolicyUseCase(searchPolicy)
                .catch {
                    Timber.e("CommunityWriteViewModel postSearchPolicy error $it")
                }
                .collectLatest {
                    setState { copy(searchPolicies = it.cachedIn(viewModelScope)) }
                }
        }
    }

    private fun uploadImage(file: File) {
        viewModelScope.launch {
            postUploadImageUseCase(file)
                .onStart {
                    setState {
                        copy(uploadLoading = true)
                    }
                    setEffect { CommunityWriteUiEffect.OnBack }
                }
                .catch {
                    Timber.e("CommunityWriteViewModel uploadImage error $it")
                }
                .collectLatest { image ->
                    Timber.e("CommunityWriteViewModel uploadImage success $image")
                    val contents = state.value.contentList.toMutableList()
                    val lastFocus = state.value.focusIndex
                    if (lastFocus.first % 2 == 0) {
                        // TextField
                        lastFocus.second?.let { textField ->
                            contents[lastFocus.first] = Contents.Text(textField.copy(text = textField.text.substring(0, textField.selection.start)))
                            val insert = contents.subList(0, lastFocus.first + 1) + listOf(
                                Contents.Image(image),
                                Contents.Text(
                                    TextFieldValue(
                                        text = textField.text.substring(textField.selection.start),
                                        selection = TextRange(textField.text.substring(textField.selection.start).length)
                                    )
                                )
                            ) + if (lastFocus.first + 1 < contents.size) contents.subList(lastFocus.first + 1, contents.size) else listOf()

                            setState {
                                copy(
                                    contentList = insert,
                                    uploadLoading = false,
                                    focusIndex = Pair(insert.lastIndex, (insert[insert.lastIndex] as? Contents.Text)?.textFieldValue)
                                )
                            }
                        }
                    } else {
                        // image
                        val insert = contents.subList(0, lastFocus.first + 1) + listOf(
                            Contents.Text(TextFieldValue("")),
                            Contents.Image(image)
                        ) + if (lastFocus.first + 1 < contents.size) contents.subList(lastFocus.first + 1, contents.size) else listOf()

                        setState {
                            copy(
                                contentList = insert,
                                uploadLoading = false,
                                focusIndex = Pair(insert.lastIndex, (insert[insert.lastIndex] as? Contents.Text)?.textFieldValue)
                            )
                        }
                    }

                    setEffect {
                        CommunityWriteUiEffect.ScrollIndex(lastFocus.first + 1)
                    }
                }
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
                .catch {
                    Timber.e("CommunityWriteViewModel getImages error $it")
                }
                .collectLatest {
                    Timber.e("CommunityWriteViewModel getImages success $it")
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
