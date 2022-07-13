package com.example.easyfitapp.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.easyfitapp.Constants
import com.example.easyfitapp.Firestore.FirestoreClass
import com.example.easyfitapp.Model.ActivityDetails
import com.example.easyfitapp.R
import com.example.easyfitapp.activity.LogActivity
import com.example.easyfitapp.adapter.ActivityDetailsAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ActivityDetailsFragment : Fragment(), View.OnClickListener {

    private lateinit var activities: ArrayList<ActivityDetails>
    private lateinit var noti: TextView
    private lateinit var name: String
    private lateinit var progress: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View = inflater.inflate(R.layout.activity_details_recycler, container, false)
        name = arguments?.getString("name").toString()
        val floatingAction: FloatingActionButton = v.findViewById(R.id.btnAddActivity)
        val btnBack: ImageView = v.findViewById(R.id.btnBack)
        val label: TextView = v.findViewById(R.id.lblActName)
        progress = v.findViewById(R.id.progressBar)
        progress.visibility = View.VISIBLE
        noti = v.findViewById(R.id.noarec)
        label.setText(name)
        floatingAction.setOnClickListener(this)
        btnBack.setOnClickListener(this)
        return v
    }

    override fun onResume() {
        val actid = arguments?.getString("id").toString()
        super.onResume()
        FirestoreClass().getActivityDetails(this@ActivityDetailsFragment, FirestoreClass().getUserID(), actid)
    }

    override fun onClick(v: View?) {
        val actid = arguments?.getString("id").toString()
        if (v != null) {
            when (v.id){
                R.id.btnAddActivity -> {
                    val intent = Intent(activity, LogActivity::class.java).apply {
                        putExtra(Constants.ACTIVITY_ID,actid)
                        putExtra("name",name)
                    }
                    startActivity(intent)
                }
                R.id.btnBack -> {
                    val activity= context as AppCompatActivity
                    val myFragment: Fragment = ActivityFragment()
                    activity.supportFragmentManager.beginTransaction()
                        .replace(R.id.main_fragment_container, myFragment).addToBackStack(null).commit()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun fetchedActivityDetails(activityDetailList: ArrayList<ActivityDetails>){
        progress.visibility = View.GONE
        activities = activityDetailList
        if (activities.isEmpty()) {
            noti.setText("No $name activities yet")
            noti.visibility = View.VISIBLE
        }
        val recycler_view = view?.findViewById<RecyclerView>(R.id.activity_details_recycler_view)
        recycler_view?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = ActivityDetailsAdapter(activities, requireActivity())
        }
    }
}