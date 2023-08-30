package com.ps.util.model

import com.ps.util.toRealmInstant


import android.annotation.SuppressLint
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId
import java.time.Instant

open class Diary : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke()
    var owner_id: String = ""
    var mood: String = ""
    var title: String = ""
    var description: String = ""
    var images: RealmList<String> = realmListOf()
    var character: String = RickAndMortyCharacters.Rick.name
    @SuppressLint("NewApi")
    var date: RealmInstant = Instant.now().toRealmInstant()
}

//import io.realm.kotlin.ext.realmListOf
//import io.realm.kotlin.types.RealmInstant
//import io.realm.kotlin.types.RealmList
//import io.realm.kotlin.types.RealmObject
//import io.realm.kotlin.types.annotations.PrimaryKey
//import org.mongodb.kbson.ObjectId
//import java.time.Instant
//
//class Diary : RealmObject {
//    @PrimaryKey
//    var _id: ObjectId = ObjectId.invoke()
//    var owner_id: String = ""
//    var title: String = ""
//    var description: String = ""
//    var images: RealmList<String> = realmListOf()
//    var date: RealmInstant = Instant.now().toRealmInstant()
//    var mood: String = Mood.Happy(character = RickAndMortyCharacters.Rick).name
//    var character: String = RickAndMortyCharacters.Rick.name
//    override fun toString(): String {
//        return "Diary(_id=$_id, ownerId='$owner_id', title='$title', description='$description', images=$images, date=$date, mood='$mood', character='$character')"
//    }
//
//
//}