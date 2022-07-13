package com.example.easyfitapp.activity

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.easyfitapp.Firestore.FirestoreClass
import com.example.easyfitapp.Model.Weight
import com.example.easyfitapp.R
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class LogWeightActivity: AppCompatActivity(), DatePickerDialog.OnDateSetListener, View.OnClickListener {

    private var day     = 0
    private var month   = 0
    private var year    = 0

    private var setDay      = 0
    private var setMonth    = 0
    private var setYear     = 0

    //Image Firebase stuff
    private lateinit var fbStorage: FirebaseStorage
    private lateinit var storageRef: StorageReference

    private lateinit var weightString: String
    private lateinit var weightDateString: String


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_weight)
        getCurrentDate()
        pickDate()
        fbStorage       = FirebaseStorage.getInstance()
        storageRef      = fbStorage.reference

        findViewById<Button>(R.id.btnLog).setOnClickListener(this)
        findViewById<TextView>(R.id.btnCancell).setOnClickListener(this)
        val input = findViewById<EditText>(R.id.txtWeight)
        findViewById<Button>(R.id.btnNewEventDtPicker).isEnabled = false

        input.setFilters(arrayOf<InputFilter>(DecimalDigitsInputFilter(5,1)))
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getCurrentDate() {
        getDateCalendar()
        val btnNewEventDtPicker = findViewById<Button>(R.id.btnNewEventDtPicker)
        btnNewEventDtPicker.text    = makeDateString(day, month + 1, year)
        weightDateString             = btnNewEventDtPicker.text.toString()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun pickDate() {
        val btnNewEventDtPicker = findViewById<Button>(R.id.btnNewEventDtPicker)
        btnNewEventDtPicker.setOnClickListener{
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
        val btnNewEventDtPicker = findViewById<Button>(R.id.btnNewEventDtPicker)
        btnNewEventDtPicker.text    = makeDateString(setDay, setMonth, setYear)
        weightDateString             = makeDateString(setDay, setMonth, setYear).toString()
    }

    private fun makeDateString(day: Int, month: Int, year: Int): String? {
        var dS: String = day.toString()
        if (day < 10){ dS = "0$day" }
        return getMonthFormat(month) + " " + dS + " " + year  }

    internal class DecimalDigitsInputFilter(digitsBeforeZero: Int, digitsAfterZero: Int) :
        InputFilter {
        private val mPattern: Pattern
        override fun filter(
            source: CharSequence,
            start: Int,
            end: Int,
            dest: Spanned,
            dstart: Int,
            dend: Int
        ): CharSequence? {
            val matcher: Matcher = mPattern.matcher(dest)
            if (!matcher.matches())
                return "" else
                return null
        }

        init {
            mPattern =
                Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?")
        }
    }
    private fun validateWeightInfo(): Boolean {
        val txtWeight = findViewById<EditText>(R.id.txtWeight)
        weightString = txtWeight.text.toString().trim { it <= ' ' }
        return when{
            TextUtils.isEmpty(txtWeight.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    this@LogWeightActivity,
                    resources.getString(R.string.err_msg_enter_weight),
                    Toast.LENGTH_SHORT).show()
                false
            }
            weightString.toDouble() < 30.0 || weightString.toDouble() > 300.0-> {
               Toast.makeText(
                    this@LogWeightActivity,
                    resources.getString(R.string.err_msg_enter_weight),
                    Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id){
                R.id.btnLog -> {
                    val txtWeight = findViewById<EditText>(R.id.txtWeight)
                    weightString = txtWeight.getText().toString().trim { it <= ' ' }
                    if (validateWeightInfo()){
                        finish()
                        addWeight()
                    }
                }
                R.id.btnCancell -> {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Discard Changes?")
                    builder.setMessage("Do you want to cancel this operation?")

                    builder.setPositiveButton(R.string.Yes) { dialog, which ->
                        finish()
                        Toast.makeText(
                            this@LogWeightActivity,
                            resources.getString(R.string.cancel_weight),
                            Toast.LENGTH_SHORT).show()
                    }
                    builder.setNegativeButton(R.string.No) { dialog, which ->
                    }
                    builder.show()
                }
            }
        }
    }
    private fun addWeight(){
        val weight = Weight(
            FirestoreClass().getUserID(),
            weightString,
            weightDateString,
            UUID.randomUUID().toString()
        )
        FirestoreClass().addWeight(this@LogWeightActivity, weight)
    }
}