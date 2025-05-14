package com.youthtalk.datasource.post

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.youthtalk.model.post.Post
import com.youthtalk.model.post.PostType

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<Post>)

    @Query("SELECT * FROM post where postType=:postType")
    fun getPagingSource(postType: PostType): PagingSource<Int, Post>

    @Query("DELETE FROM post where postType=:postType")
    fun deleteAll(postType: PostType)

    @Update
    suspend fun updatePost(post: Post)

    @Query("DELETE FROM post WHERE postId=:postId AND postType=:postType")
    suspend fun deletePost(postId: Long, postType: PostType)
}
