package com.example.easyfitapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.easyfitapp.GlideLoader
import com.example.easyfitapp.Model.Video
import com.example.easyfitapp.R
import com.example.easyfitapp.fragments.ViewVideoFragment


open class VideoAdapter(
    private val videoList: ArrayList<Video>,
    private val context: Context
) : RecyclerView.Adapter<VideoAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var thumbnail: ImageView = itemView.findViewById(R.id.thumbnail)
        var title: TextView = itemView.findViewById(R.id.videoTitle)

        init {
            itemView.setOnClickListener {
                var position: Int = bindingAdapterPosition
                val videourl = videoList[position].url
                val videotitle = videoList[position].title
                val videothumb = videoList[position].thumbnail
                val desc = videoList[position].desc
                val context = itemView.context
                val activity = context as AppCompatActivity
                val myFragment: Fragment = ViewVideoFragment()

                val id = Bundle()
                id.putString("url", videourl)
                id.putString("title",videotitle)
                id.putString("thumb",videothumb)
                id.putString("desc",desc)

                myFragment.arguments = id
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.main_fragment_container, myFragment).addToBackStack(null).commit()
                }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_video, viewGroup, false)
        return ViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val video = videoList[position]
        holder.title.text = video.title
        GlideLoader(context).loadImage(video.thumbnail,holder.thumbnail)
    }

    override fun getItemCount(): Int {
        return videoList.size
    }
}


