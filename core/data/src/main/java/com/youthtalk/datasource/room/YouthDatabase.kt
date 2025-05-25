package com.youthtalk.datasource.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.youthtalk.datasource.policy.PolicyDao
import com.youthtalk.datasource.policy.PolicyRemoteKey
import com.youthtalk.datasource.policy.PolicyRemoteKeyDao
import com.youthtalk.datasource.post.PostDao
import com.youthtalk.datasource.post.PostRemoteKey
import com.youthtalk.datasource.post.PostRemoteKeyDao
import com.youthtalk.model.policy.Policy
import com.youthtalk.model.post.Post

@Database(
    entities = [
        Post::class,
        PostRemoteKey::class,
        Policy::class,
        PolicyRemoteKey::class
    ],
    version = 4,
    autoMigrations = [AutoMigration(1, 2), AutoMigration(2, 3), AutoMigration(3, 4)],
    exportSchema = true
)
@TypeConverters(value = [DateTimeConverter::class])
abstract class YouthDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun postRemoteKeyDao(): PostRemoteKeyDao
    abstract fun policyRemoteKeyDao(): PolicyRemoteKeyDao
    abstract fun policyDao(): PolicyDao

    companion object {

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
            CREATE TABLE IF NOT EXISTS `Post_new` (
                `postId` INTEGER NOT NULL,
                `title` TEXT NOT NULL,
                `writerId` INTEGER,
                `policyId` INTEGER,
                `policyTitle` TEXT,
                `comments` INTEGER NOT NULL,
                `contentPreview` TEXT NOT NULL,
                `scrapCount` INTEGER NOT NULL,
                `scrap` INTEGER NOT NULL,
                `createdAt` TEXT NOT NULL,
                `category` TEXT,
                `postType` TEXT NOT NULL,
                PRIMARY KEY(`postId`, 'postType')
            )
                    """.trimIndent()
                )

                // 2. 기존 데이터 복사
                database.execSQL(
                    """
            INSERT INTO `Post_new` (
                postId, title, writerId, policyId, policyTitle,
                comments, contentPreview, scrapCount, scrap,
                createdAt, category, postType
            )
            SELECT
                postId, title, writerId, policyId, policyTitle,
                comments, contentPreview, scrapCount, scrap,
                createdAt, category, postType
            FROM Post
                    """.trimIndent()
                )

                // 3. 기존 테이블 삭제
                database.execSQL("DROP TABLE Post")

                // 4. 새 테이블 이름 변경
                database.execSQL("ALTER TABLE Post_new RENAME TO Post")
            }
        }
    }
}
