package com.ps.dimensional_feels.data.repository

import com.ps.dimensional_feels.model.Diary
import com.ps.dimensional_feels.util.RequestState
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

typealias Diaries = RequestState<Map<LocalDate, List<Diary>>>

interface MongoRepository {
    fun configureTheRealm()
    fun getAllDiaries(): Flow<Diaries>

    suspend fun insertDiary(diary: Diary) : RequestState<Diary>
}