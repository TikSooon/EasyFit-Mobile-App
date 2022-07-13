package com.example.easyfitapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Filter
import android.widget.Filterable
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.easyfitapp.Firestore.FirestoreClass
import com.example.easyfitapp.Model.Activity
import com.example.easyfitapp.R
import com.example.easyfitapp.adapter.ActivityAdapter
import java.util.*
import kotlin.collections.ArrayList


class ActivityFragment : Fragment() {

    private lateinit var activities: ArrayList<Activity>
    private lateinit var adapter: ActivityAdapter
    private lateinit var progress: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.activity_recycler, container, false)

        progress = v.findViewById(R.id.progressBar)
        progress.visibility = View.VISIBLE
        val search = v.findViewById<SearchView>(R.id.searchA)
        val recycler_view = v.findViewById<RecyclerView>(R.id.activity_recycler_view)

        search.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                var resultList = ArrayList<Activity>()
                if (query == null) {
                    resultList = activities
                } else {
                    for (row in activities) {
                        if (row.name.lowercase(Locale.ROOT)
                                .contains(query.lowercase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                }
                adapter = ActivityAdapter(resultList, requireActivity())
                recycler_view?.adapter = adapter
                return true
            }
        })

        return v
    }

    override fun onResume() {
        super.onResume()
        FirestoreClass().getActivity(this@ActivityFragment)
    }

    fun fetchedActivity(activityList: ArrayList<Activity>) {
        progress.visibility = View.GONE
        activities = activityList
        val recycler_view = view?.findViewById<RecyclerView>(R.id.activity_recycler_view)
        recycler_view?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = ActivityAdapter(activities, requireActivity())
        }
    }

}