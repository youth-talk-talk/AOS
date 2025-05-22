package com.youthtalk.dto.home

import com.youthtalk.dto.policy.PoliciesWithReviewResponse
import com.youthtalk.dto.policy.PolicyResponse
import com.youthtalk.dto.post.PostResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomeDataResponse(
    val popularPolicies: List<PolicyResponse>,
    val policiesWithReviews: List<PoliciesWithReviewResponse>,
    val bestPosts: List<PostResponse>
)

@Serializable
data class NewPoliciesResponse(
    @SerialName("ALL")
    val all: List<PolicyResponse>,
    @SerialName("JOB")
    val job: List<PolicyResponse>,
    @SerialName("DWELLING")
    val dwelling: List<PolicyResponse>,
    @SerialName("EDUCATION")
    val education: List<PolicyResponse>,
    @SerialName("LIFE")
    val life: List<PolicyResponse>,
    @SerialName("PARTICIPATION")
    val participation: List<PolicyResponse>
)
