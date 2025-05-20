package com.youthtalk.datasource.post

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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

    @Query(
        """
        UPDATE post
            SET
                scrap = :scrap,
                scrapCount = CASE
                    WHEN scrap = 0 THEN scrapCount + 1
                    ELSE scrapCount - 1
                END
            WHERE postId = :postId;
    """
    )
    suspend fun updatePostScrap(postId: Long, scrap: Boolean)

    @Query("SELECT * FROM post where postId=:postId limit 1")
    suspend fun getPost(postId: Long): Post?

    @Query("DELETE FROM post WHERE postId=:postId")
    suspend fun deletePost(postId: Long)

    @Query("SELECT * FROM post where postType=:postType AND scrap=1")
    fun getScrapPagingSource(postType: PostType): PagingSource<Int, Post>
}
