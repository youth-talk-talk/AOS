package com.youthtalk.repository

import com.core.dataapi.repository.CommentRepository
import com.core.exception.NoDataException
import com.youthtalk.data.CommentService
import com.youthtalk.dto.comment.AddCommentRequest
import com.youthtalk.dto.comment.CommentResponse
import com.youthtalk.dto.comment.ModifyCommentRequest
import com.youthtalk.mapper.toData
import com.youthtalk.model.CommentInfo
import com.youthtalk.utils.ErrorUtils.throwableError
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CommentRepositoryImpl @Inject constructor(
    private val commentService: CommentService
) : CommentRepository {
    override fun getPolicyComment(policyId: String): Flow<CommentInfo> = flow {
        runCatching {
            commentService.getPolicyComment(policyId)
        }
            .onSuccess { response ->
                emit(response.data?.toData() ?: CommentInfo(0, listOf()))
            }
            .onFailure {
                throwableError<List<CommentResponse>>(it)
            }
    }

    override fun getPostDetailComments(postId: Long): Flow<CommentInfo> = flow {
        runCatching {
            commentService.getPostDetailComments(postId)
        }
            .onSuccess { response ->
                emit(response.data?.toData() ?: CommentInfo(0, listOf()))
            }
            .onFailure {
                throwableError<List<CommentResponse>>(it)
            }
    }

    override fun postPostAddComment(postId: Long, message: String): Flow<Long> = flow {
        val requestBody = AddCommentRequest(postId, message).toRequestBody()
        runCatching {
            commentService.postPostAddComment(requestBody)
        }
            .onSuccess { response ->
                response.data?.let { data ->
                    emit(data.commentId)
                } ?: throw NoDataException()
            }
            .onFailure {
                throwableError<List<CommentResponse>>(it)
            }
    }

    override fun patchComment(commentId: Long, message: String): Flow<Long> = flow {
        val requestBody = ModifyCommentRequest(commentId, message).toRequestBody()
        runCatching {
            commentService.patchComment(requestBody)
        }
            .onSuccess { _ ->
                emit(commentId)
            }
            .onFailure {
                throwableError<List<CommentResponse>>(it)
            }
    }
}
