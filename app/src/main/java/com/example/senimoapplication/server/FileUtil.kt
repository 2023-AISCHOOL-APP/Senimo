package com.example.senimoapplication.server

// FileUtil.kt
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore

object FileUtil {
    // URI를 실제 파일 경로로 변환하는 함수입니다.
    fun getPath(context: Context, uri: Uri): String? {
        var cursor: Cursor? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA) // 파일 경로를 얻기 위한 컬럼명

        try {
            cursor = context.contentResolver.query(uri, projection, null, null, null)
            val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            if (cursor?.moveToFirst() == true) {
                // 실제 경로를 반환합니다.
                return cursor.getString(columnIndex!!)
            }
        } finally {
            cursor?.close()
        }
        return null
    }
}
