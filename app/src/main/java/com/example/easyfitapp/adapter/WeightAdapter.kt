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
import com.example.easyfitapp.Model.Weight
import com.example.easyfitapp.R
import com.example.easyfitapp.activity.MainActivity
import com.example.easyfitapp.activity.UpdateWeightActivity

class WeightAdapter(
    private val weightList: ArrayList<Weight>,
    private val context: Context
) : RecyclerView.Adapter<WeightAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var date: TextView = itemView.findViewById(R.id.cardview_date)
        var dailyweight: TextView = itemView.findViewById(R.id.cardview_daily_weight)

        init {
            itemView.setOnClickListener {
                var position: Int = bindingAdapterPosition
                val context = itemView.context
                val weightID = weightList[position].id
                val intent = Intent(context, UpdateWeightActivity::class.java).apply {
                    putExtra(Constants.WEIGHT_ID, weightID)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_weight, viewGroup, false)
        return ViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val weight = weightList[position]
        holder.date.text       = weight.date
        holder.dailyweight.text    = weight.weight
    }

    override fun getItemCount(): Int {
        return weightList.size
    }
}


