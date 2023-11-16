//package com.example.senimoapplication.server
//
//import android.Manifest
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.net.Uri
//import android.os.Bundle
//import android.provider.MediaStore
//import android.util.Log
//import android.widget.Button
//import android.widget.ImageView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import com.example.senimoapplication.R
//import com.example.senimoapplication.server.Retrofit.ApiService
//import okhttp3.MediaType.Companion.toMediaTypeOrNull
//import okhttp3.MultipartBody
//import okhttp3.RequestBody
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import java.io.File
//import java.util.UUID
//
//
//// 서버 응답을 위한 데이터 클래스 정의
//data class ResponseDC(val message: String, val fileId: Int)
//
//class picture_test : AppCompatActivity() {
//    private lateinit var imageView: ImageView
//    private val pickImage = 100 //  갤러리에서 사진을 선택하는 행위를 구분하기 위한 요청코드
//    private var imageUri: Uri? = null //사용자가 갤러리에서 선택한 사진의 '주소(URI)'를 저장
//
//    // Retrofit 객체 생성
//    val retrofit = Retrofit.Builder()
//        .baseUrl("http://192.168.70.44:3000") // 실제 서버의 IP나 호스트네임으로 변경하세요
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//
//    // API 서비스 인스턴스 생성
//    val service = retrofit.create(ApiService::class.java)
//
//    // 필요한 권한 목록
//    private val requiredPermissions = arrayOf(
//        Manifest.permission.READ_EXTERNAL_STORAGE,
//        Manifest.permission.WRITE_EXTERNAL_STORAGE
//    )
//
//    // 권한 요청에 대한 결과를 처리하는 코드
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        // 권한 요청 결과를 확인
//        if (requestCode == REQUEST_PERMISSIONS) { // 권한요청 식별
//            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) { // 권한부여 확인
//                // 모든 권한이 부여된 경우
//                Toast.makeText(this, "권한이 부여되었습니다.", Toast.LENGTH_SHORT).show()
//            } else {
//                // 권한이 거부된 경우
//                Toast.makeText(this, "권한 거부로 인해 기능을 사용할 수 없습니다.", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_picture_test)
//
//        imageView = findViewById(R.id.imageView)
//        val buttonUpload: Button = findViewById(R.id.button_upload)
//        // 권한이 있는지 확인하고 없으면 요청
//        if (!hasPermissions(requiredPermissions)) {
//            ActivityCompat.requestPermissions(this, requiredPermissions, REQUEST_PERMISSIONS)
//        }
//        buttonUpload.setOnClickListener {
//            // 권한이 있을 경우 갤러리를 열고, 없으면 권한 요청
//            if (hasPermissions(requiredPermissions)) {
//                openGallery()
//            } else {
//
//                ActivityCompat.requestPermissions(this, requiredPermissions, REQUEST_PERMISSIONS)
//            }
//        }
//    }
//
//    private fun openGallery() {
//        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        startActivityForResult(galleryIntent, pickImage)
//    }
//
//    private fun hasPermissions(permissions: Array<String>): Boolean {
//        return permissions.all {
//            ContextCompat.checkSelfPermission(
//                this,
//                it
//            ) == PackageManager.PERMISSION_GRANTED
//        }
//    }
//
//    // ... (기존 onActivityResult 및 나머지 코드)
//
//    companion object {
//        private const val REQUEST_PERMISSIONS = 1 //1은 식별자로써 특별한 의미는 없음
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == RESULT_OK && requestCode == pickImage) {
//            imageUri = data?.data
//            imageView.setImageURI(imageUri)
//            val filePath = FileUtil.getPath(this, imageUri!!)
//            if (filePath != null) {
//                val file = File(filePath)
//                val requestFile =
//                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
//                val uniqueFileName = "${file.name}+${UUID.randomUUID()}"
//                val body = MultipartBody.Part.createFormData("file", uniqueFileName, requestFile)
//
//                service.postImg(body).enqueue(object : Callback<ResponseDC> {
//                    override fun onResponse(
//                        call: Call<ResponseDC>,
//                        response: Response<ResponseDC>
//                    ) {
//                        Log.d("PictureTest", "resopnse1: " + response.message())
//                        if (response.isSuccessful) {
//                            Log.d("PictureTest", "Upload success: " + response.body()?.message)
//                            Toast.makeText(
//                                this@picture_test,
//                                "업로드 성공5: " + response.body()?.message,
//                                Toast.LENGTH_LONG
//                            ).show()
//                        } else {
//                            Log.d("PictureTest", "업로드에 실패했습니다: " + response.errorBody()?.string())
//                            Toast.makeText(this@picture_test, "업로드에 실패했습니다5.", Toast.LENGTH_LONG)
//                                .show()
//                        }
//                    }
//
//                    override fun onFailure(call: Call<ResponseDC>, t: Throwable) {
//                        t.printStackTrace()
//                        Log.e("PictureTest", "네트워크 요청 실패", t)
//                        Toast.makeText(
//                            this@picture_test,
//                            "서버 연결에 실패했습니다5: " + t.message,
//                            Toast.LENGTH_LONG
//                        ).show()
//                    }
//                })
//            } else {
//                Toast.makeText(this, "파일 경로를 찾을 수 없습니다5.", Toast.LENGTH_SHORT).show()
//                Log.e("PictureTest", "File path is null")
//            }
//        }
//    }
//}