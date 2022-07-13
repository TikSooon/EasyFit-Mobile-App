package com.example.easyfitapp.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.easyfitapp.*
import com.example.easyfitapp.Firestore.FirestoreClass
import com.example.easyfitapp.Model.Weight
import com.example.easyfitapp.activity.LogWeightActivity
import com.example.easyfitapp.adapter.WeightAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton



class WeightFragment : Fragment(), View.OnClickListener{
    private lateinit var weight: ArrayList<Weight>
    private lateinit var noti: TextView
    private lateinit var progress: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.weight_recycler, container, false)
        val floatingAction: FloatingActionButton = v.findViewById(R.id.btnAddWeight)
        progress = v.findViewById(R.id.progressBar)
        noti = v.findViewById(R.id.norec)
        progress.visibility = View.VISIBLE
        floatingAction.setOnClickListener(this)
        return v
    }

    override fun onResume() {
        super.onResume()
        FirestoreClass().getWeight(this@WeightFragment, FirestoreClass().getUserID())
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id){
                R.id.btnAddWeight -> {
                    val intent = Intent(activity, LogWeightActivity::class.java)
                    startActivity(intent)
                }}}
    }

    fun fetchedWeight(weightList: ArrayList<Weight>){
        weight = weightList
        progress.visibility = View.GONE
        if (weight.isEmpty()) {
            noti.visibility = View.VISIBLE
        }
        else{
        val recycler_view = view?.findViewById<RecyclerView>(R.id.recycler_view)
        recycler_view?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = WeightAdapter(weight, requireActivity())}
        }
    }
}