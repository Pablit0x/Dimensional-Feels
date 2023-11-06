package com.ps.dimensional_feels.di

import android.content.Context
import com.ps.dimensional_feels.util.PreferencesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModules {

    @Provides
    @Singleton
    fun providePreferencesManager(@ApplicationContext context: Context) : PreferencesManager {
        return PreferencesManager(context = context)
    }

}