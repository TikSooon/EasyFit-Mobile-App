package com.example.easyfitapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.easyfitapp.Constants
import com.example.easyfitapp.Model.Activity
import com.example.easyfitapp.Model.ActivityDetails
import com.example.easyfitapp.Model.Weight
import com.example.easyfitapp.R
import com.example.easyfitapp.activity.MainActivity
import com.example.easyfitapp.activity.UpdateActivity

open class ActivityDetailsAdapter(
    private val activityDetailsList: ArrayList<ActivityDetails>,
    private val context: Context
) : RecyclerView.Adapter<ActivityDetailsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var date: TextView = itemView.findViewById(R.id.cardview_adate)
        var weight: TextView = itemView.findViewById(R.id.cardview_aweight)
        var set: TextView = itemView.findViewById(R.id.cardview_set)
        var rep: TextView = itemView.findViewById(R.id.cardview_rep)

        init {
            itemView.setOnClickListener {
                var position: Int = bindingAdapterPosition
                val context = itemView.context
                val activityID = activityDetailsList[position].id
                val intent = Intent(context, UpdateActivity::class.java).apply {
                    putExtra(Constants.ACTIVITY_ID, activityID)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_activity_details, viewGroup, false)
        return ViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val activitydetails = activityDetailsList[position]
        holder.date.text = activitydetails.date
        holder.weight.text = activitydetails.weight
        holder.set.text = activitydetails.set
        holder.rep.text = activitydetails.rep
    }

    override fun getItemCount(): Int {
        return activityDetailsList.size
    }
}


