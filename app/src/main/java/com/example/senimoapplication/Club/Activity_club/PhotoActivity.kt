package com.example.senimoapplication.Club.Activity_club

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.indexOf
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.senimoapplication.Club.VO.GalleryVO
import com.example.senimoapplication.Club.adapter.PhotoViewAdapter
import com.example.senimoapplication.Common.photoUploadTime
import com.example.senimoapplication.R
import com.example.senimoapplication.databinding.ActivityPhotoBinding

class PhotoActivity : AppCompatActivity() {
    lateinit var binding : ActivityPhotoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPhotoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // 넘어온 데이터
        val clickedPhoto = intent.getSerializableExtra("clickedPhoto") as? GalleryVO
        val originData = intent.getSerializableExtra("photos") as ArrayList<GalleryVO>
        // 로그로 clickedPhoto 내용 출력
        Log.d("PhotoActivity1", "Clicked photo: ${clickedPhoto.toString()}")

        // 로그로 originData 리스트의 크기 출력
        Log.d("PhotoActivity2", "clickOrigin data size: ${originData?.size}")

        // originData 리스트의 내용을 로그로 출력 (예: 처음 5개 항목)
        originData?.take(5)?.forEachIndexed { index, photo ->
            Log.d("PhotoActivity3", "click Photo at index $index: $photo")}



        val selectedIndex = originData.indexOfFirst { it.imgUri == clickedPhoto?.imgUri }
        val adapter = PhotoViewAdapter(this,this, R.layout.photo_view_list, originData)

        // 뷰페이저 설정
        val viewPager: ViewPager2 = binding.viewPagerPhoto
        viewPager.adapter = adapter

        viewPager.setPageTransformer { page, position ->
            val offset = 40 // 페이지 사이 간격 조절 (원하는 간격으로 조절)
            val scaleFactor = 0.85f // 페이지 크기 조절 (원하는 크기로 조절)

            val absPosition = Math.abs(position)
                if (absPosition >= 1) {
                    // 현재 페이지 외의 페이지 크기 설정
                    page.alpha = scaleFactor
                } else {
                    page.translationX = -offset * position
                    page.alpha = 1 - (absPosition * (1 - scaleFactor))
                }
        }

        // 페이지별로 끊어서 이동
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvPhoto)

        // 리사이클러뷰 설정
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvPhoto.layoutManager = layoutManager
        binding.rvPhoto.adapter = adapter

        // ViewPager2의 스와이프 동작을 감지하고 RecyclerView에 전달
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // RecyclerView를 ViewPager2의 현재 페이지 위치로 스크롤합니다.
                layoutManager.scrollToPositionWithOffset(position, 0)
            }
        })

        if (selectedIndex > -1) {
            viewPager.setCurrentItem(selectedIndex, false)}
        Log.d("click selecetdIndex", "$selectedIndex")

        // RecyclerView의 스와이프 동작을 ViewPager2에 전달
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // 현재 스와이프한 아이템의 위치
                val currentPosition = viewHolder.adapterPosition

                // 이전에 선택한 아이템의 위치
                val previousPosition = viewPager.currentItem

                if (currentPosition > previousPosition) {
                    // 오른쪽으로 스와이프한 경우 다음 아이템으로 이동
                    viewPager.currentItem = previousPosition + 1
                } else if (currentPosition < previousPosition) {
                    // 왼쪽으로 스와이프한 경우 이전 아이템으로 이동
                    viewPager.currentItem = previousPosition - 1
                }
            }
        })

        itemTouchHelper.attachToRecyclerView(binding.rvPhoto)



    }
}

class PhotoPageChangeCallback(private val recyclerView: RecyclerView) : ViewPager2.OnPageChangeCallback() {
    override fun onPageSelected(position: Int) {
        // ViewPager2 페이지가 변경될 때 현재 아이템을 리사이클러뷰에서 표시
        recyclerView.scrollToPosition(position)
    }
}
