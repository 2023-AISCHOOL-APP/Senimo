package com.example.senimoapplication.Club.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.senimoapplication.Club.Activity_club.ClubActivity
import com.example.senimoapplication.Club.Activity_club.PhotoActivity
import com.example.senimoapplication.Club.VO.GalleryVO
import com.example.senimoapplication.R

class GalleryAdapter(val context: Context, val layout: Int, val data: ArrayList<GalleryVO>) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {
    val inflater = LayoutInflater.from(context)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView
        init {
            img = view.findViewById(R.id.img)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryAdapter.ViewHolder {
        val view = inflater.inflate(layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: GalleryAdapter.ViewHolder, position: Int) {
        //holder.img.setImageURI(data[position].imageUrl.toUri())
        holder.img.setImageResource(R.drawable.img_sample)

        holder.img.setOnClickListener {
            val intent = Intent(context, PhotoActivity::class.java)
            intent.putExtra("clickedPhoto", data[position])
            context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
        return data.size
    }
}
