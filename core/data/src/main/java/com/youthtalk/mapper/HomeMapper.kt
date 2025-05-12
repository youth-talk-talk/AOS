package com.youthtalk.mapper

import com.youthtalk.dto.home.HomeDataResponse
import com.youthtalk.dto.home.NewPoliciesResponse
import com.youthtalk.dto.policy.PoliciesWithReviewResponse
import com.youthtalk.dto.policy.PolicyResponse
import com.youthtalk.dto.policy.ReviewResponse
import com.youthtalk.dto.post.PostResponse
import com.youthtalk.model.community.Post
import com.youthtalk.model.home.HomeData
import com.youthtalk.model.home.NewPolicies
import com.youthtalk.model.policy.PoliciesWithReview
import com.youthtalk.model.policy.Policy
import com.youthtalk.model.policy.Review
import java.time.LocalDateTime

fun HomeDataResponse.toDomain(): HomeData = HomeData(
    popularPolicies = popularPolicies.map { it.toDomain() },
    policiesWithReviews = policiesWithReviews.map { it.toDomain() },
    bestPosts = bestPosts.map { it.toDomain() }
)

fun PolicyResponse.toDomain(): Policy = Policy(
    policyId = policyId,
    category = category,
    title = title,
    deadlineStatus = deadlineStatus,
    hostDep = hostDep,
    scrapCount = scrapCount,
    departmentImgUrl = departmentImgUrl,
    region = region,
    scrap = scrap
)

fun NewPoliciesResponse.toDomain(): NewPolicies = NewPolicies(
    all = all.map { it.toDomain() },
    job = job.map { it.toDomain() },
    dwelling = dwelling.map { it.toDomain() },
    education = education.map { it.toDomain() },
    life = life.map { it.toDomain() },
    participation = participation.map { it.toDomain() }
)

fun PoliciesWithReviewResponse.toDomain(): PoliciesWithReview = PoliciesWithReview(
    policyId = policyId,
    title = title,
    departmentImgUrl = departmentImgUrl,
    reviews = reviews.map { it.toDomain() }
)

fun ReviewResponse.toDomain(): Review = Review(
    postId = postId,
    title = title,
    contentPreview = contentPreview,
    commentCount = commentCount,
    scrapCount = scrapCount,
    createdAt = LocalDateTime.parse(createdAt.replace(" ", "T")),
    scrap = scrap
)

fun PostResponse.toDomain(): Post = Post(
    postId = postId,
    title = title,
    writerId = writerId,
    policyId = policyId,
    policyTitle = policyTitle,
    comments = comments,
    contentPreview = contentPreview,
    scraps = scraps,
    scrap = scrap,
    createdAt = LocalDateTime.parse(createdAt.replace(" ", "T"))
)
