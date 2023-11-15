package com.example.senimoapplication.Club.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.senimoapplication.R

class GalleryAdapter (val context: Context, val layout : Int, val data : ArrayList<Uri>) : RecyclerView.Adapter<GalleryAdapter.ViewHolder> () {
    val inflater = LayoutInflater.from(context)
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val img : ImageView
        init {
            img = view.findViewById(R.id.img)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryAdapter.ViewHolder {
        val view = inflater.inflate(layout, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: GalleryAdapter.ViewHolder, position: Int) {
        holder.img.setImageURI(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }


}