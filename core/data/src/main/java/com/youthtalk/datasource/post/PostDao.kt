package com.youthtalk.datasource.post

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.youthtalk.model.Post

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<Post>)

    @Query("SELECT * FROM post ORDER BY postId DESC")
    fun getPagingSource(): PagingSource<Int, Post>

    @Query("DELETE FROM post")
    fun deleteAll()

    @Query("SELECT * FROM post WHERE postId=:postId")
    suspend fun getPostById(postId: Long): Post?

    @Update
    suspend fun updatePost(post: Post)

    @Query("DELETE FROM post WHERE postId=:postId")
    suspend fun deletePost(postId: Long)
}
