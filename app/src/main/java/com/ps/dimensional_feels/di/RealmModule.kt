package com.ps.dimensional_feels.di

import com.ps.dimensional_feels.data.repository.MongoRepository
import com.ps.dimensional_feels.data.repository.MongoRepositoryImpl
import com.ps.dimensional_feels.model.Diary
import com.ps.dimensional_feels.util.Constants.APP_ID
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.User
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RealmModule {

    @Provides
    @Singleton
    fun provideRealmApp(): App {
        return App.create(APP_ID)
    }

    @Provides
    @Singleton
    fun provideRealmUser(app: App): User? {
        return app.currentUser
    }

    @Provides
    @Singleton
    fun provideRealm(user: User?): Realm? {
        return if (user == null) {
            null
        } else {
            val config =
                SyncConfiguration.Builder(user, setOf(Diary::class)).initialSubscriptions { sub ->
                    add(
                        query = sub.query<Diary>("owner_id == $0", user.id)

                    )
                }.build()
            Realm.open(config)
        }
    }

    @Provides
    @Singleton
    fun provideMongoRepository(user: User?, realm: Realm?): MongoRepository {
        return MongoRepositoryImpl(user = user, realm = realm)
    }

}