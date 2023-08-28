package com.ps.dimensional_feels.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ps.dimensional_feels.data.database.entity.ImageToUpload

@Database(
    entities = [ImageToUpload::class], version = 1, exportSchema = false
)
abstract class ImagesDatabase : RoomDatabase() {
    abstract fun imageToUploadDao(): ImageToUploadDao
}