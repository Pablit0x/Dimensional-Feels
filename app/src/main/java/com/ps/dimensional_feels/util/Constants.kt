package com.ps.dimensional_feels.util

import android.Manifest

object Constants {
    const val APP_ID = "dimensional-feels-nhoal"
    const val CLIENT_ID = "17268274910-90f4ghads21n894s1f4ntrapf48slgr0.apps.googleusercontent.com"
    const val DATE_PATTERN = "dd MMM yyyy"
    const val TIME_PATTERN = "hh:mm a"
    const val DATE_TIME_PATTERN = "$DATE_PATTERN, $TIME_PATTERN"

    const val IMAGE_TO_UPLOAD_TABLE = "image_to_upload_table"
    const val IMAGE_DATABASE = "images_db"
    const val IMAGE_TO_DELETE_TABLE = "image_to_delete_table"

    internal val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    internal const val PERMISSION_CODE = 100

}