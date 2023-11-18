package com.ps.dimensional_feels.data.repository

import android.util.Log
import com.ps.dimensional_feels.model.Diary
import com.ps.dimensional_feels.util.RequestState
import com.ps.dimensional_feels.util.exceptions.QueriedDiaryDoesNotExist
import com.ps.dimensional_feels.util.exceptions.UserNotAuthenticatedException
import com.ps.dimensional_feels.util.toInstant
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.mongodb.User
import io.realm.kotlin.query.Sort
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

class MongoRepositoryImpl @Inject constructor(
    private val user: User?,
    private val realm: Realm?
) : MongoRepository {
    override fun getAllDiaries(): Flow<Diaries> {
        return if (user != null && realm != null) {
            try {
                realm.query<Diary>(query = "owner_id == $0", user.id)
                    .sort(property = "date", sortOrder = Sort.DESCENDING).asFlow().map { result ->
                        RequestState.Success(data = result.list.groupBy {
                            it.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                        })
                    }
            } catch (e: Exception) {
                flow { emit(RequestState.Error(e)) }
            }
        } else {
            flow { emit(RequestState.Error(UserNotAuthenticatedException())) }
        }
    }

    override fun getTextFilteredDiaries(searchText: String): Flow<Diaries> {
        return if (user != null && realm != null) {
            try {
                realm.query<Diary>(
                    "owner_id == $0 AND title CONTAINS[c] $1 OR description CONTAINS[c] $1",
                    user.id,
                    searchText
                ).asFlow().map { result ->
                    RequestState.Success(data = result.list.groupBy {
                        it.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                    })
                }
            } catch (e: Exception) {
                flow { emit(RequestState.Error(e)) }
            }
        } else {
            flow { emit(RequestState.Error(UserNotAuthenticatedException())) }
        }
    }

    override fun getDateFilteredDiaries(zonedDateTime: ZonedDateTime): Flow<Diaries> {
        return if (user != null && realm != null) {
            try {
                realm.query<Diary>(
                    "owner_id == $0 AND date < $1 AND date > $2",
                    user.id,
                    RealmInstant.from(
                        LocalDateTime.of(
                            zonedDateTime.toLocalDate().plusDays(1), LocalTime.MIDNIGHT
                        ).toEpochSecond(zonedDateTime.offset), 0
                    ),
                    RealmInstant.from(
                        LocalDateTime.of(
                            zonedDateTime.toLocalDate(), LocalTime.MIDNIGHT
                        ).toEpochSecond(zonedDateTime.offset), 0
                    ),

                    ).asFlow().map { result ->
                    RequestState.Success(data = result.list.groupBy {
                        it.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                    })
                }
            } catch (e: Exception) {
                flow { emit(RequestState.Error(e)) }
            }
        } else {
            flow { emit(RequestState.Error(UserNotAuthenticatedException())) }
        }
    }

    override fun getSelectedDiary(diaryId: ObjectId): Flow<RequestState<Diary>> {
        return if (user != null && realm != null) {
            try {
                realm.query<Diary>(query = "_id == $0", diaryId).asFlow().map {
                    RequestState.Success(data = it.list.first())
                }
            } catch (e: Exception) {
                flow { emit(RequestState.Error(e)) }
            }
        } else {
            flow { emit(RequestState.Error(UserNotAuthenticatedException())) }
        }
    }

    override suspend fun insertDiary(diary: Diary): RequestState<Diary> {
        return if (user != null && realm != null) {
            realm.write {
                try {
                    val addedDiary = copyToRealm(diary.apply { owner_id = user.id })
                    RequestState.Success(data = addedDiary)
                } catch (e: Exception) {
                    RequestState.Error(e)
                }
            }
        } else {
            RequestState.Error(UserNotAuthenticatedException())
        }
    }

    override suspend fun updateDiary(diary: Diary): RequestState<Diary> {
        return if (user != null && realm != null) {
            realm.write {
                val queriedDiary = query<Diary>("_id == $0", diary._id).find().first()
                run {
                    queriedDiary.title = diary.title
                    queriedDiary.description = diary.description
                    queriedDiary.mood = diary.mood
                    queriedDiary.character = diary.character
                    queriedDiary.images = diary.images
                    queriedDiary.date = diary.date
                    RequestState.Success(data = queriedDiary)
                }
            }
        } else {
            RequestState.Error(UserNotAuthenticatedException())
        }
    }

    override suspend fun deleteDiary(diaryId: ObjectId): RequestState<Boolean> {
        return if (user != null && realm != null) {
            realm.write {
                val diary =
                    query<Diary>(query = "_id == $0 AND owner_id == $1", diaryId, user.id).first()
                        .find()
                if (diary != null) {
                    try {
                        delete(diary)
                        RequestState.Success(data = true)
                    } catch (e: Exception) {
                        RequestState.Error(e)
                    }
                } else {
                    RequestState.Error(QueriedDiaryDoesNotExist())
                }
            }
        } else {
            RequestState.Error(UserNotAuthenticatedException())
        }
    }

    override suspend fun deleteAllDiaries(): RequestState<Boolean> {
        return if (user != null && realm != null) {
            realm.write {
                val diaries = this.query<Diary>("owner_id = $0", user.id).find()
                try {
                    delete(diaries)
                    RequestState.Success(data = true)
                } catch (e: Exception) {
                    RequestState.Error(e)
                }
            }
        } else {
            RequestState.Error(UserNotAuthenticatedException())
        }
    }

    override suspend fun transferAllDiariesToGoogleAccount(anonymousId: String): RequestState<Boolean> {
        TODO("Not yet implemented")
    }
}