package com.core.domain.usercase.comment

import com.core.dataapi.repository.CommentRepository
import javax.inject.Inject

class PostCommentLikeUseCase @Inject constructor(
    private val commentRepository: CommentRepository
) {
    suspend operator fun invoke(commentId: Long, isLike: Boolean) = commentRepository.postLikes(commentId, isLike)
}
