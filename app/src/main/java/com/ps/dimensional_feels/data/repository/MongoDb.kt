package com.ps.dimensional_feels.data.repository

import com.ps.dimensional_feels.model.Diary
import com.ps.dimensional_feels.util.Constants.APP_ID
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration

object MongoDb : MongoRepository {

    private val app = App.create(APP_ID)
    private val user = app.currentUser
    private lateinit var realm: Realm
    override fun configureTheRealm() {
        user?.let { user ->
            val config = SyncConfiguration.Builder(
                user = user, schema = setOf(Diary::class)
            ).initialSubscriptions { sub ->
                add(
                    query = sub.query("ownerId == $0", user.id), name = "Diaries"
                )
            }.build()
            realm = Realm.open(config)
        }
    }
}