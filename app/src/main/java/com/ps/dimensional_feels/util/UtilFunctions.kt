package com.ps.dimensional_feels.util

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.alpha
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.google.firebase.storage.FirebaseStorage
import com.ps.dimensional_feels.util.Constants.PERMISSION_CODE
import com.ps.dimensional_feels.util.Constants.permissions
import io.realm.kotlin.types.RealmInstant
import java.io.File
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

fun Long.toLocalDate(): LocalDate {
    val instant = Instant.ofEpochMilli(this)
    return instant.atZone(ZoneId.systemDefault()).toLocalDate()
}

fun RealmInstant.toInstant(): Instant {
    val sec: Long = this.epochSeconds
    val nano: Int = this.nanosecondsOfSecond
    return if (sec >= 0) {
        Instant.ofEpochSecond(sec, nano.toLong())
    } else {
        Instant.ofEpochSecond(sec - 1, 1_000_000 + nano.toLong())
    }
}


//https://www.mongodb.com/docs/realm/sdk/kotlin/realm-database/schemas/supported-types/
fun Instant.toRealmInstant(): RealmInstant {
    val sec: Long = this.epochSecond
    val nano: Int = this.nano
    return if (sec >= 0) {
        RealmInstant.from(sec, nano)
    } else {
        RealmInstant.from(sec + 1, -1_000_000 + nano)
    }
}

fun fetchImagesFromFirebase(
    remoteImagePaths: List<String>,
    onImageDownload: (Uri) -> Unit,
    onImageDownloadFailed: (Exception) -> Unit = {},
    onReadyToDisplay: () -> Unit = {}
) {
    if (remoteImagePaths.isNotEmpty()) {
        remoteImagePaths.forEachIndexed { index, remoteImagePath ->
            val trimmedRemoteImagePath = remoteImagePath.trim()
            if (trimmedRemoteImagePath.isNotEmpty()) {
                FirebaseStorage.getInstance().reference.child(trimmedRemoteImagePath).downloadUrl.addOnSuccessListener {
                    onImageDownload(it)
                    if (remoteImagePaths.lastIndexOf(remoteImagePaths.last()) == index) {
                        onReadyToDisplay()
                    }
                }.addOnFailureListener {
                    onImageDownloadFailed(it)
                }
            }
        }
    }
}

fun Color.convertToOldColor(): Int {
    val color = this.toArgb()
    return android.graphics.Color.argb(
        color.alpha,
        color.red,
        color.green,
        color.blue
    )
}

fun Context.saveImage(bitmap: Bitmap): Uri? {
    var uri: Uri? = null
    try {
        val fileName = System.nanoTime().toString() + ".png"
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/")
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        }

        uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        uri?.let {
            contentResolver.openOutputStream(it).use { output ->
                if (output != null) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)
                }
            }
            values.apply {
                clear()
                put(MediaStore.Audio.Media.IS_PENDING, 0)
            }
            contentResolver.update(uri, values, null, null)
        }
        return uri
    } catch (e: java.lang.Exception) {
        if (uri != null) {
            // Don't leave an orphan entry in the MediaStore
            contentResolver.delete(uri, null, null)
        }
        throw e
    }
}


