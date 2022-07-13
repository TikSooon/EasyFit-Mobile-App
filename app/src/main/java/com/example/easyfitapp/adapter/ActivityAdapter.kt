package com.example.easyfitapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.easyfitapp.Model.Activity
import com.example.easyfitapp.R
import com.example.easyfitapp.fragments.ActivityDetailsFragment
import java.util.*
import kotlin.collections.ArrayList


open class ActivityAdapter(
    private val activityList: ArrayList<Activity>,
    private val context: Context
) : RecyclerView.Adapter<ActivityAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var activity: TextView = itemView.findViewById(R.id.cardview_actlabel)

        init {
            itemView.setOnClickListener {
                var position: Int = bindingAdapterPosition
                val activityID = activityList[position].id
                val activityname = activityList[position].name
                val context = itemView.context

                val activity= context as AppCompatActivity
                val myFragment: Fragment = ActivityDetailsFragment()

                val id = Bundle()
                id.putString("id", activityID)
                id.putString("name",activityname)
                Log.e(activityID,activityname)
                myFragment.arguments = id
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.main_fragment_container, myFragment).addToBackStack(null).commit()
                }
        } }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_activity, viewGroup, false)
        return ViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val activity = activityList[position]
        holder.activity.text = activity.name
    }

    override fun getItemCount(): Int {
        return activityList.size
    }
}


