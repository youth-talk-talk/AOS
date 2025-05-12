package com.youthtalk.model.home

import com.youthtalk.model.community.Post
import com.youthtalk.model.policy.PoliciesWithReview
import com.youthtalk.model.policy.Policy

data class HomeData(
    val popularPolicies: List<Policy>,
    val policiesWithReviews: List<PoliciesWithReview>,
    val bestPosts: List<Post>
)

data class NewPolicies(
    val all: List<Policy>,
    val job: List<Policy>,
    val dwelling: List<Policy>,
    val education: List<Policy>,
    val life: List<Policy>,
    val participation: List<Policy>
)
