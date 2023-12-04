package com.example.senimoapplication.Club.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.senimoapplication.Club.Activity_club.PhotoActivity
import com.example.senimoapplication.Club.VO.loadGalleryVO
import com.example.senimoapplication.R

class GalleryAdapter(val context: Context, val layout: Int, val data: ArrayList<loadGalleryVO>) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {
    val inflater = LayoutInflater.from(context)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView
        init {
            img = view.findViewById(R.id.img)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("galleryList6: ", data[position].toString())
        Glide.with(context).load(data[position].imgThumbName).into(holder.img)
        Glide.with(context).load(data[position].userImg)
        Log.d("adapter동작","4444444444444")
        Log.d("adapter동작중인가", data.size.toString())
        holder.img.setOnClickListener {
            val intent = Intent(context, PhotoActivity::class.java)
            intent.putExtra("clickedPhoto", data[position])
            Log.d("clickedphoto check", "${data[position]}")
            intent.putExtra("photos", data)
            Log.d("clickeddata check", "${data}")
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
