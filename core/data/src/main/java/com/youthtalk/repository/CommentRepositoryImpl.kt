package com.youthtalk.repository

import android.util.Log
import com.core.dataapi.repository.CommentRepository
import com.core.exception.NoDataException
import com.youthtalk.data.CommentService
import com.youthtalk.mapper.toDate
import com.youthtalk.model.Comment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val commentService: CommentService,
) : CommentRepository {
    override fun getPolicyComment(policyId: String): Flow<List<Comment>> = flow {
        runCatching {
            commentService.getPolicyComment(policyId)
        }
            .onSuccess { response ->
                response.data?.let { comments ->
                    emit(comments.map { it.toDate() })
                } ?: throw NoDataException("no Data")
            }
            .onFailure {
                Log.d("YOON-CHAN", "CommentRepository getPolicyComment error ${it.message}")
            }
    }
}
