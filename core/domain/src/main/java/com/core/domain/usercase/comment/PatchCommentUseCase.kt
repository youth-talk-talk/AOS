package com.core.domain.usercase.comment

import com.core.dataapi.repository.CommentRepository
import javax.inject.Inject

class PatchCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository
) {
    suspend operator fun invoke(commentId: Long, message: String) = commentRepository.patchComment(commentId, message)
}
