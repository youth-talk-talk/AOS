package com.youthtalk.di

import android.content.Context
import androidx.room.Room
import com.youthtalk.datasource.room.YouthDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): YouthDatabase {
        return Room.databaseBuilder(
            context = context,
            name = "youth",
            klass = YouthDatabase::class.java,
        ).build()
    }
}
