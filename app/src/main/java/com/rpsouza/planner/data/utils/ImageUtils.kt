package com.rpsouza.planner.data.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import java.io.ByteArrayOutputStream

fun Context.imageUriToBitmap(uri: Uri): Bitmap? {
    return try {
        val contentResolver = this.contentResolver
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(contentResolver, uri)
        }
    } catch (_: Exception) {
        null
    }
}

fun imageBitmapToBase64(bitmap: Bitmap): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

fun imageBase64ToBitmap(base46String: String): Bitmap? {
    return try {
        val decodeBytes = Base64.decode(base46String, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodeBytes, 0, decodeBytes.size)
    } catch (_: Exception) {
        null
    }
}