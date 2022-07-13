package com.example.easyfitapp.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
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
import java.util.*

class LogActivity: AppCompatActivity(), DatePickerDialog.OnDateSetListener, View.OnClickListener {

    private var day     = 0
    private var month   = 0
    private var year    = 0

    private var setDay      = 0
    private var setMonth    = 0
    private var setYear     = 0

    private lateinit var activityID: String
    private lateinit var name: String
    //Image Firebase stuff
    private lateinit var fbStorage: FirebaseStorage
    private lateinit var storageRef: StorageReference

    private lateinit var weight: String
    private lateinit var dateString: String
    private lateinit var set: String
    private lateinit var rep: String
    private lateinit var lblLog: TextView


    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_activity)
        val intent = intent
        activityID = intent.getStringExtra(Constants.ACTIVITY_ID).toString()
        name = intent.getStringExtra("name").toString()
        Log.e("id",activityID)
        getCurrentDate()
        pickDate()
        fbStorage       = FirebaseStorage.getInstance()
        storageRef      = fbStorage.reference
        lblLog = findViewById(R.id.lblLogA)
        lblLog.setText("Log $name")
        findViewById<Button>(R.id.btnLogA).setOnClickListener(this)
        findViewById<TextView>(R.id.btnCancelA).setOnClickListener(this)
        findViewById<Button>(R.id.btnDt).isEnabled = false
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getCurrentDate() {
        getDateCalendar()
        val btnDt = findViewById<Button>(R.id.btnDt)
        btnDt.text = makeDateString(day, month + 1, year)
        dateString = btnDt.text.toString()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun pickDate() {
        val btnDt = findViewById<Button>(R.id.btnDt)
        btnDt.setOnClickListener{
            getDateCalendar()
            DatePickerDialog(this, this, year, month,day).show()
        }
    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun getDateCalendar(){
        val calendar = Calendar.getInstance()
        day     = calendar.get(Calendar.DAY_OF_MONTH)
        month   = calendar.get(Calendar.MONTH)
        year    = calendar.get(Calendar.YEAR)
    }

    private fun getMonthFormat(month: Int): String {
        if (month == 1) return "Jan"
        if (month == 2) return "Feb"
        if (month == 3) return "Mar"
        if (month == 4) return "Apr"
        if (month == 5) return "May"
        if (month == 6) return "Jun"
        if (month == 7) return "Jul"
        if (month == 8) return "Aug"
        if (month == 9) return "Sep"
        if (month == 10) return "Oct"
        if (month == 11) return "Nov"
        return if (month == 12) "Dec" else "???"
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        setDay      = dayOfMonth
        setMonth    = month + 1
        setYear     = year
        val btnDt = findViewById<Button>(R.id.btnDt)
        btnDt.text    = makeDateString(setDay, setMonth, setYear)
        dateString = makeDateString(setDay, setMonth, setYear).toString()
    }

    private fun makeDateString(day: Int, month: Int, year: Int): String? {
        var dS: String = day.toString()
        if (day < 10){ dS = "0$day" }
        return getMonthFormat(month) + " " + dS + " " + year  }

    private fun validateActivityInfo(): Boolean {
        val txtweight = findViewById<EditText>(R.id.txtaweight)
        val txtset = findViewById<EditText>(R.id.txtset)
        val txtrep = findViewById<EditText>(R.id.txtrep)
        weight = txtweight.text.toString().trim { it <= ' ' }
        set = txtset.text.toString().trim { it <= ' ' }
        rep = txtrep.text.toString().trim { it <= ' ' }
        return when{
            TextUtils.isEmpty(txtweight.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    this@LogActivity,
                    resources.getString(R.string.err_msg_enter_weight),
                    Toast.LENGTH_SHORT).show()
                false
            }
            TextUtils.isEmpty(txtset.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    this@LogActivity,
                    resources.getString(R.string.err_msg_enter_set),
                    Toast.LENGTH_SHORT).show()
                false
            }
            TextUtils.isEmpty(txtrep.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    this@LogActivity,
                    resources.getString(R.string.err_msg_enter_rep),
                    Toast.LENGTH_SHORT).show()
                false
            }
            Integer.parseInt(weight) < 1 || Integer.parseInt(weight) > 500-> {
               Toast.makeText(
                    this@LogActivity,
                    resources.getString(R.string.err_msg_enter_weight),
                    Toast.LENGTH_SHORT).show()
                false
            }
            Integer.parseInt(set) < 0 || Integer.parseInt(set) > 10-> {
                Toast.makeText(
                    this@LogActivity,
                    resources.getString(R.string.err_msg_enter_set),
                    Toast.LENGTH_SHORT).show()
                false
            }
            Integer.parseInt(rep) < 0 || Integer.parseInt(rep) > 30-> {
                Toast.makeText(
                    this@LogActivity,
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
                R.id.btnLogA -> {
                    weight = findViewById<EditText>(R.id.txtaweight).text.toString().trim { it <= ' ' }
                    set = findViewById<EditText>(R.id.txtset).text.toString().trim { it <= ' ' }
                    rep = findViewById<EditText>(R.id.txtrep).text.toString().trim { it <= ' ' }
                    if (validateActivityInfo()){
                        finish()
                        addActivity()
                    }
                }
                R.id.btnCancelA -> {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Discard Changes?")
                    builder.setMessage("Do you want to cancel this operation?")
                    builder.setPositiveButton(R.string.Yes) { dialog, which ->
                        finish()
                        Toast.makeText(
                            this@LogActivity,
                            resources.getString(R.string.cancel_activity),
                            Toast.LENGTH_SHORT).show()
                    }
                    builder.setNegativeButton(R.string.No) { dialog, which ->
                    }
                    builder.show()
                }
            }
        }
    }

    private fun addActivity(){
        val activity = ActivityDetails(
            FirestoreClass().getUserID(),
            activityID,
            dateString,
            weight,
            set,
            rep,
            UUID.randomUUID().toString()
        )
        FirestoreClass().addActivityDetails(this@LogActivity, activity)
    }

}