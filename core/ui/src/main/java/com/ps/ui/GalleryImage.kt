package com.ps.ui

import android.net.Uri

data class GalleryImage(
    val image: Uri,
    val remoteImagePath: String = ""
)
