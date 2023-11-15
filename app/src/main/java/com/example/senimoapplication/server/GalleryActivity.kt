package com.example.senimoapplication.server

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.senimoapplication.R
import com.example.senimoapplication.server.Retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GalleryActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var photoAdapter: PhotoAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        loadPhotos()
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        // 여기서는 버튼을 클릭하면 무슨 일이 일어나게 할지,
//        // 리스트에 어떤 목록을 보여줄지 같은 '세부 설정'을 할 수 있어요.
//    } 프래그먼트에서 사용

    override fun onResume() {
        super.onResume()
        // 사용자가 앱으로 돌아왔을 때 필요한 작업을 여기서 해요.
        // 예를 들어, 목록을 최신 상태로 갱신하거나, 게임을 다시 시작하는 등의 일이죠.
        loadPhotos()
    }

    private fun loadPhotos() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.70.44:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ApiService::class.java)

        service.getPhotos().enqueue(object : Callback<List<Photo>> {
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                Log.d("GalleryActivity1", response.toString())
                if (response.isSuccessful) {
                    val photosList = response.body() ?: emptyList()
                    Log.d("GalleryActivity", response.body().toString())
                    photoAdapter = PhotoAdapter(photosList)
                    recyclerView.adapter = photoAdapter
                } else {
                    Log.e("GalleryActivity", "통신은 성공했으나 요청에 실패했습니다: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                Log.e("GalleryActivity", "네트워크 요청 실패", t)
            }
        })
    }
}
