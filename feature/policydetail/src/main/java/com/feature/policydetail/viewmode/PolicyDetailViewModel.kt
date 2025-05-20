package com.feature.policydetail.viewmode

import androidx.lifecycle.ViewModel
import com.core.domain.usercase.GetUserUseCase
import com.core.domain.usercase.PostPolicyAddCommentUseCase
import com.core.domain.usercase.PostPolicyScrapUseCase
import com.core.domain.usercase.comment.PostDeleteCommentUseCase
import com.core.domain.usercase.policydetail.GetPolicyDetailCommentUseCase
import com.core.domain.usercase.policydetail.GetPolicyDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PolicyDetailViewModel @Inject constructor(
    private val getPolicyDetailUseCase: GetPolicyDetailUseCase,
    private val getPolicyDetailCommentUseCase: GetPolicyDetailCommentUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val postPolicyScrapUseCase: PostPolicyScrapUseCase,
    private val postPolicyAddCommentUseCase: PostPolicyAddCommentUseCase,
    private val postDeleteCommentUseCase: PostDeleteCommentUseCase
) : ViewModel()
