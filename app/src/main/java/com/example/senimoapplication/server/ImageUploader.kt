package com.example.senimoapplication.server

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ImageUploader(private val context: Context) {
    // Uri에서 실제 파일 경로를 얻는 메소드
    fun getRealPathFromURI(contentResolver: ContentResolver, contentUri: Uri): String {
        var cursor = contentResolver.query(contentUri, null, null, null, null)
        cursor?.moveToFirst()
        val idx = cursor?.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        val result = cursor?.getString(idx!!)
        cursor?.close()
        return result ?: ""
    }
    // 이미지 파일을 멀티파트 형식으로 준비하는 메소드
    fun prepareImagePart(fileUri: Uri): MultipartBody.Part {
        val filePath = getRealPathFromURI(context.contentResolver, fileUri)
        val file = File(filePath)

        // 파일을 RequestBody로 변환
        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())

        // MultipartBody.Part 생성 및 반환
        return MultipartBody.Part.createFormData("picture", file.name, requestFile)
    }
}
