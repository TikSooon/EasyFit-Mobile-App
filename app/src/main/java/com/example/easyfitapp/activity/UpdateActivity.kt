package com.example.easyfitapp.activity

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.easyfitapp.Constants
import com.example.easyfitapp.Firestore.FirestoreClass
import com.example.easyfitapp.Model.Activity
import com.example.easyfitapp.Model.ActivityDetails
import com.example.easyfitapp.Model.Weight
import com.example.easyfitapp.R
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class UpdateActivity: AppCompatActivity(), View.OnClickListener {

    private lateinit var activityID: String

    //Image Firebase stuff
    private lateinit var fbStorage: FirebaseStorage
    private lateinit var storageRef: StorageReference

    private lateinit var weight: String
    private lateinit var set: String
    private lateinit var rep: String

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_activity)

        fbStorage = FirebaseStorage.getInstance()
        storageRef = fbStorage.reference

        activityID = intent.extras!!.getString(Constants.ACTIVITY_ID).toString()

        findViewById<Button>(R.id.btnUpdateA).setOnClickListener(this)
        findViewById<TextView>(R.id.btnCancelU).setOnClickListener(this)
        findViewById<Button>(R.id.btnUADt).isEnabled = false

        getActivityDetails()
    }

    private fun getActivityDetails() {
        FirestoreClass().getUpdateActivityDetails(this, activityID)
    }

    fun gotUpdateActivityDetails(activity: ActivityDetails){
        findViewById<TextView>(R.id.txtaweightu).text = activity.weight
        findViewById<TextView>(R.id.txtsetu).text = activity.set
        findViewById<TextView>(R.id.txtrepu).text = activity.rep
        findViewById<TextView>(R.id.btnUADt).text = activity.date
    }

    private fun validateActivityInfo(): Boolean {
        val txtweight = findViewById<EditText>(R.id.txtaweightu)
        val txtset = findViewById<EditText>(R.id.txtsetu)
        val txtrep = findViewById<EditText>(R.id.txtrepu)
        weight = txtweight.text.toString().trim { it <= ' ' }
        set = txtset.text.toString().trim { it <= ' ' }
        rep = txtrep.text.toString().trim { it <= ' ' }
        return when{
            TextUtils.isEmpty(txtweight.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    this@UpdateActivity,
                    resources.getString(R.string.err_msg_enter_weight),
                    Toast.LENGTH_SHORT).show()
                false
            }
            TextUtils.isEmpty(txtset.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    this@UpdateActivity,
                    resources.getString(R.string.err_msg_enter_set),
                    Toast.LENGTH_SHORT).show()
                false
            }
            TextUtils.isEmpty(txtrep.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    this@UpdateActivity,
                    resources.getString(R.string.err_msg_enter_rep),
                    Toast.LENGTH_SHORT).show()
                false
            }
            Integer.parseInt(weight) < 1 || Integer.parseInt(weight) > 500-> {
                Toast.makeText(
                    this@UpdateActivity,
                    resources.getString(R.string.err_msg_enter_weight),
                    Toast.LENGTH_SHORT).show()
                false
            }
            Integer.parseInt(set) < 0 || Integer.parseInt(set) > 10-> {
                Toast.makeText(
                    this@UpdateActivity,
                    resources.getString(R.string.err_msg_enter_set),
                    Toast.LENGTH_SHORT).show()
                false
            }
            Integer.parseInt(rep) < 0 || Integer.parseInt(rep) > 30-> {
                Toast.makeText(
                    this@UpdateActivity,
                    resources.getString(R.string.err_msg_enter_rep),
                    Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id){
                R.id.btnUpdateA -> {
                    weight = findViewById<EditText>(R.id.txtaweightu).text.toString().trim { it <= ' ' }
                    set = findViewById<EditText>(R.id.txtsetu).text.toString().trim { it <= ' ' }
                    rep = findViewById<EditText>(R.id.txtrepu).text.toString().trim { it <= ' ' }
                    if (validateActivityInfo()){
                        finish()
                        updateActivity()
                    }
                }
                R.id.btnCancelU -> {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Discard Changes?")
                    builder.setMessage("Do you want to cancel this operation?")
                    builder.setPositiveButton(R.string.Yes) { dialog, which ->
                        finish()
                        Toast.makeText(
                            this@UpdateActivity,
                            resources.getString(R.string.cancel_uactivity),
                            Toast.LENGTH_SHORT).show()
                    }
                    builder.setNegativeButton(R.string.No) { dialog, which ->
                    }
                    builder.show()
                }
            }
        }
    }
    private fun updateActivity(){
        val activityHashMap = HashMap<String, Any>()

        activityHashMap["rep"] = rep
        activityHashMap["set"] = set
        activityHashMap["weight"] = weight

        FirestoreClass().updateActivity(
            this@UpdateActivity,
            activityHashMap, activityID
        )
    }
}