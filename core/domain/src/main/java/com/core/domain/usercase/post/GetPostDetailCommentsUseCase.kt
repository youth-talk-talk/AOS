package com.core.domain.usercase.post

import com.core.dataapi.repository.CommentRepository
import javax.inject.Inject

class GetPostDetailCommentsUseCase @Inject constructor(
    private val commentRepository: CommentRepository
) {
    operator fun invoke(postId: Long) = commentRepository.getPostDetailComments(postId)
}
