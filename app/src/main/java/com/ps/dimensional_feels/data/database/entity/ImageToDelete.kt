package com.ps.dimensional_feels.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ps.dimensional_feels.util.Constants.IMAGE_TO_DELETE_TABLE

@Entity(tableName = IMAGE_TO_DELETE_TABLE)
data class ImageToDelete(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val remoteImagePath: String
)
