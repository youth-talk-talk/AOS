package com.youthtalk.datasource.review

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.youthtalk.model.ReviewPost

@Dao
interface ReviewPostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<ReviewPost>)

    @Query("SELECT * FROM reviewpost ORDER BY postId DESC")
    fun getPagingSource(): PagingSource<Int, ReviewPost>

    @Query("DELETE FROM reviewpost")
    fun deleteAll()

    @Query("SELECT * FROM reviewpost WHERE postId=:postId")
    suspend fun getPostById(postId: Long): ReviewPost?

    @Update
    suspend fun updatePost(post: ReviewPost)

    @Query("DELETE FROM reviewpost WHERE postId=:postId")
    suspend fun deletePost(postId: Long)
}
