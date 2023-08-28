package com.ps.dimensional_feels.di

import android.content.Context
import androidx.room.Room
import com.ps.dimensional_feels.data.database.ImagesDatabase
import com.ps.dimensional_feels.util.Constants.IMAGE_DATABASE
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
    fun provideDatabase(
        @ApplicationContext context: Context
    ): ImagesDatabase {
        return Room.databaseBuilder(
            context = context, klass = ImagesDatabase::class.java, name = IMAGE_DATABASE
        ).build()
    }

    @Provides
    @Singleton
    fun provideImageToUploadDao(database: ImagesDatabase) = database.imageToUploadDao()
}