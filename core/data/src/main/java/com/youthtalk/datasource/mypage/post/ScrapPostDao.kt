package com.youthtalk.datasource.mypage.post

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.youthtalk.model.ScrapPost

@Dao
interface ScrapPostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<ScrapPost>)

    @Query("SELECT * FROM scrappost WHERE scrap=1 ORDER BY scrapId DESC")
    fun getScrapPostPagingSource(): PagingSource<Int, ScrapPost>

    @Query("SELECT * FROM scrappost ORDER BY postId DESC")
    fun getPostPagingSource(): PagingSource<Int, ScrapPost>

    @Query("DELETE FROM scrappost")
    fun deleteAll()

    @Query("SELECT * FROM scrappost WHERE postId=:postId")
    suspend fun getPostById(postId: Long): ScrapPost?

    @Query("DELETE FROM scrappost WHERE postId=:postId")
    suspend fun deletePost(postId: Long)

    @Update
    suspend fun updatePost(post: ScrapPost)
}
