package com.youthtalk.repository

import com.core.dataapi.repository.CommentRepository
import com.core.exception.NoDataException
import com.youthtalk.data.CommentService
import com.youthtalk.dto.comment.AddCommentRequest
import com.youthtalk.dto.comment.CommentLikeRequest
import com.youthtalk.dto.comment.ModifyCommentRequest
import com.youthtalk.mapper.toData
import com.youthtalk.model.comment.CommentInfo
import com.youthtalk.utils.ErrorUtils.createResult
import com.youthtalk.utils.ErrorUtils.throwableError
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CommentRepositoryImpl @Inject constructor(
    private val commentService: CommentService
) : CommentRepository {
    override suspend fun getPolicyComment(policyId: Long): Result<CommentInfo> = createResult {
        commentService.getPolicyComment(policyId).data?.toData() ?: CommentInfo(0, listOf())
    }

    override suspend fun getPostDetailComments(postId: Long): Result<CommentInfo> = createResult {
        commentService.getPostDetailComments(postId).data?.toData() ?: CommentInfo(0, listOf())
    }

    override suspend fun postPostAddComment(postId: Long, message: String): Result<Long> = createResult {
        val requestBody = AddCommentRequest(postId, message).toRequestBody()
        commentService.postPostAddComment(requestBody).data?.commentId ?: throw NoDataException()
    }

    override suspend fun patchComment(commentId: Long, message: String): Result<Long> = createResult {
        val requestBody = ModifyCommentRequest(commentId, message).toRequestBody()
        commentService.patchComment(requestBody)
        commentId
    }

    override fun postLikes(commentId: Long, isLike: Boolean): Flow<String> = flow {
        val requestBody = CommentLikeRequest(commentId, !isLike).toRequestBody()
        runCatching {
            commentService.postLikes(requestBody)
        }
            .onSuccess { response ->
                emit(response.message)
            }
            .onFailure {
                throwableError<Unit>(it)
            }
    }
}
