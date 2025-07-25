package com.core.domain.usercase.comment

import com.core.dataapi.repository.CommentRepository
import javax.inject.Inject

class PostAddPostCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository
) {
    suspend operator fun invoke(postId: Long, message: String) = commentRepository.postPostAddComment(postId, message)
}
