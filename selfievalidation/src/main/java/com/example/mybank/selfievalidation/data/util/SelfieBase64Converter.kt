package com.example.mybank.selfievalidation.data.util

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream

class SelfieBase64Converter {
    fun bitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
    }
}

