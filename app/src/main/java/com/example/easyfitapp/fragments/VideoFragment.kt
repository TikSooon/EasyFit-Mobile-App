package com.example.easyfitapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.easyfitapp.Firestore.FirestoreClass
import com.example.easyfitapp.Model.Activity
import com.example.easyfitapp.Model.Video
import com.example.easyfitapp.R
import com.example.easyfitapp.adapter.ActivityAdapter
import com.example.easyfitapp.adapter.VideoAdapter
import java.util.*
import kotlin.collections.ArrayList

class VideoFragment : Fragment() {

    private lateinit var video: ArrayList<Video>
    private lateinit var adapter: VideoAdapter
    private lateinit var progress: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v: View = inflater.inflate(R.layout.video_recycler, container, false)
        val search = v.findViewById<SearchView>(R.id.searchV)
        val recycler_view = v.findViewById<RecyclerView>(R.id.video_recycler_view)
        progress = v.findViewById(R.id.progressBar)
        progress.visibility = View.VISIBLE
        search.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                var resultList = ArrayList<Video>()
                if (query == null) {
                    resultList = video
                } else {
                    for (row in video) {
                        if (row.title.lowercase(Locale.ROOT)
                                .contains(query.lowercase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                }
                adapter = VideoAdapter(resultList, requireActivity())
                recycler_view?.adapter = adapter
                return true
            }
        })

        return v
    }

    override fun onResume() {
        super.onResume()
        FirestoreClass().getVideo(this@VideoFragment)
    }
    //
    fun fetchedVideo(videoList: ArrayList<Video>){
        progress.visibility = View.GONE
        video = videoList
        val recycler_view = view?.findViewById<RecyclerView>(R.id.video_recycler_view)
        recycler_view?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = VideoAdapter(video, requireActivity())
        }
    }

}