package com.ps.dimensional_feels.di

import android.content.Context
import com.ps.dimensional_feels.alarm.AlarmScheduler
import com.ps.dimensional_feels.data.alarm.AndroidAlarmScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AlarmModule {

    @Provides
    @Singleton
    fun provideAlarmScheduler(
        @ApplicationContext context: Context
    ) : AlarmScheduler {
        return AndroidAlarmScheduler(context = context)
    }
}