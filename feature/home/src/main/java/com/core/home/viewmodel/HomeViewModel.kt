package com.core.home.viewmodel

import androidx.lifecycle.viewModelScope
import com.core.base.BaseViewModel
import com.core.domain.usercase.GetUserUseCase
import com.core.domain.usercase.PostPolicyScrapUseCase
import com.core.domain.usercase.home.GetHomeDataUseCase
import com.core.domain.usercase.home.GetNewPolicesUseCase
import com.core.domain.usercase.post.PostPostScrapUseCase
import com.core.domain.usercase.user.PostUserUseCase
import com.core.home.model.home.HomeUiEffect
import com.core.home.model.home.HomeUiEvent
import com.core.home.model.home.HomeUiState
import com.youthtalk.model.User
import com.youthtalk.model.typeenum.Region
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeDataUseCase: GetHomeDataUseCase,
    private val getNewPolicesUseCase: GetNewPolicesUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val postUserUseCase: PostUserUseCase,
    private val postPolicyScrapUseCase: PostPolicyScrapUseCase,
    private val postPostScrapUseCase: PostPostScrapUseCase
) : BaseViewModel<HomeUiState, HomeUiEvent, HomeUiEffect>(
    initialState = HomeUiState()
) {

    init {
        setEvent(HomeUiEvent.GetHomeData())
    }

    override fun handleEvents(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.GetHomeData -> getHomeData(isLoading = event.isLoading)
            is HomeUiEvent.PostRegion -> postUser(event.user, event.region)
            is HomeUiEvent.PostPolicyScrap -> postPolicyScrap(event.policyId, event.scrap)
            is HomeUiEvent.PostPostScrap -> postPostScrap(event.postId, event.scrap)
        }
    }

    private fun postPostScrap(postId: Long, scrap: Boolean) {
        viewModelScope.launch {
            postPostScrapUseCase(postId, scrap)
                .onSuccess {
                    setState {
                        copy(
                            homeData = homeData.copy(
                                policiesWithReviews = homeData.policiesWithReviews.map {
                                    it.copy(
                                        reviews = it.reviews.map { review ->
                                            if (review.postId == postId) {
                                                review.copy(
                                                    scrap = !scrap,
                                                    scrapCount = review.scrapCount + (if (scrap) -1 else 1)
                                                )
                                            } else {
                                                review
                                            }
                                        }
                                    )
                                },
                                bestPosts = homeData.bestPosts.map { post ->
                                    if (post.postId == postId) {
                                        post.copy(
                                            scrap = !scrap,
                                            scrapCount = post.scrapCount + (if (scrap) -1 else 1)
                                        )
                                    } else {
                                        post
                                    }
                                }
                            )
                        )
                    }
                }
        }
    }

    private fun postPolicyScrap(policyId: Long, scrap: Boolean) {
        viewModelScope.launch {
            postPolicyScrapUseCase(policyId, scrap)
                .catch {
                    Timber.e("HomeViewModel postPolicyScrap error $it")
                }
                .collectLatest {
                    Timber.e("HomeViewModel postPolicyScrap success $it")
                    setState {
                        copy(
                            homeData = homeData.copy(
                                popularPolicies = homeData.popularPolicies.map { policy ->
                                    if (policy.policyId == policyId) {
                                        policy
                                            .copy(scrap = !scrap, scrapCount = policy.scrapCount + (if (scrap) -1 else 1))
                                    } else {
                                        policy
                                    }
                                }

                            ),
                            newPolicies = newPolicies.copy(
                                all = newPolicies.all.map { policy ->
                                    if (policy.policyId == policyId) {
                                        policy
                                            .copy(scrap = !scrap, scrapCount = policy.scrapCount + (if (scrap) -1 else 1))
                                    } else {
                                        policy
                                    }
                                },
                                job = newPolicies.job.map { policy ->
                                    if (policy.policyId == policyId) {
                                        policy
                                            .copy(scrap = !scrap, scrapCount = policy.scrapCount + (if (scrap) -1 else 1))
                                    } else {
                                        policy
                                    }
                                },
                                dwelling = newPolicies.dwelling.map { policy ->
                                    if (policy.policyId == policyId) {
                                        policy
                                            .copy(scrap = !scrap, scrapCount = policy.scrapCount + (if (scrap) -1 else 1))
                                    } else {
                                        policy
                                    }
                                },
                                education = newPolicies.education.map { policy ->
                                    if (policy.policyId == policyId) {
                                        policy
                                            .copy(scrap = !scrap, scrapCount = policy.scrapCount + (if (scrap) -1 else 1))
                                    } else {
                                        policy
                                    }
                                },
                                life = newPolicies.life.map { policy ->
                                    if (policy.policyId == policyId) {
                                        policy
                                            .copy(scrap = !scrap, scrapCount = policy.scrapCount + (if (scrap) -1 else 1))
                                    } else {
                                        policy
                                    }
                                },
                                participation = newPolicies.all.map { policy ->
                                    if (policy.policyId == policyId) {
                                        policy
                                            .copy(scrap = !scrap, scrapCount = policy.scrapCount + (if (scrap) -1 else 1))
                                    } else {
                                        policy
                                    }
                                }
                            )
                        )
                    }
                }
        }
    }

    private fun postUser(user: User, region: Region) {
        viewModelScope.launch {
            postUserUseCase(user.nickname, region)
                .catch {
                    Timber.e("HomeViewModel postUser error $it")
                }
                .collectLatest {
                    setEvent(HomeUiEvent.GetHomeData())
                }
        }
    }

    private fun getHomeData(isLoading: Boolean = true) {
        viewModelScope.launch {
            val homeData = getHomeDataUseCase()
            val newPolicies = getNewPolicesUseCase()

            getUserUseCase()
                .onStart {
                    setState { copy(isLoading = isLoading) }
                }.catch {
                    Timber.e("error : $it")
                }.collectLatest { user ->
                    setState {
                        HomeUiState(
                            isLoading = false,
                            user = user,
                            homeData = homeData.getOrThrow(),
                            newPolicies = newPolicies.getOrThrow()
                        )
                    }
                }
        }
    }
}
