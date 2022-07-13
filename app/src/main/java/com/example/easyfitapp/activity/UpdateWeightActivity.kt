package com.example.easyfitapp.activity

import android.os.Build
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
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
import com.example.easyfitapp.Model.Weight
import com.example.easyfitapp.R
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.regex.Matcher
import java.util.regex.Pattern

class UpdateWeightActivity: AppCompatActivity(), View.OnClickListener {

    private lateinit var weightID: String

    //Image Firebase stuff
    private lateinit var fbStorage: FirebaseStorage
    private lateinit var storageRef: StorageReference

    private lateinit var weightString: String

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_weight)

        fbStorage = FirebaseStorage.getInstance()
        storageRef = fbStorage.reference

        weightID = intent.extras!!.getString(Constants.WEIGHT_ID).toString()

        findViewById<Button>(R.id.btnUpdateW).setOnClickListener(this)
        findViewById<TextView>(R.id.btnCancelUW).setOnClickListener(this)
        findViewById<Button>(R.id.btnUDtPicker).isEnabled = false

        val input = findViewById<EditText>(R.id.txtUpdateW)

        input.setFilters(arrayOf<InputFilter>(LogWeightActivity.DecimalDigitsInputFilter(5, 1)))
        getWeightDetails()
    }

    private fun getWeightDetails() {
        FirestoreClass().getWeightDetails(this, weightID)
    }

    fun gotWeightDetails(weight: Weight){
        findViewById<TextView>(R.id.txtUpdateW).text = weight.weight
        findViewById<TextView>(R.id.btnUDtPicker).text = weight.date
    }

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
        val txtWeight = findViewById<EditText>(R.id.txtUpdateW)
        weightString = txtWeight.text.toString().trim { it <= ' ' }
        return when {
            TextUtils.isEmpty(txtWeight.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    this@UpdateWeightActivity,
                    resources.getString(R.string.err_msg_enter_weight),
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
            weightString.toDouble() < 30.0 || weightString.toDouble() > 300.0-> {
                Toast.makeText(
                    this@UpdateWeightActivity,
                    resources.getString(R.string.err_msg_enter_weight),
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
            else -> true
        }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id){
                R.id.btnUpdateW -> {
                    weightString = findViewById<EditText>(R.id.txtUpdateW).text.toString().trim { it <= ' ' }
                    if (validateWeightInfo()){
                        finish()
                        updateWeight()
                    }
                }
                R.id.btnCancelUW -> {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Discard Changes?")
                    builder.setMessage("Do you want to cancel this operation?")
                    builder.setPositiveButton(R.string.Yes) { dialog, which ->
                        finish()
                        Toast.makeText(
                            this@UpdateWeightActivity,
                            resources.getString(R.string.cancel_uweight),
                            Toast.LENGTH_SHORT).show()
                    }
                    builder.setNegativeButton(R.string.No) { dialog, which ->
                    }
                    builder.show()
                }
            }
        }
    }
    private fun updateWeight(){
        val weightHashMap = HashMap<String, Any>()

        weightHashMap[Constants.WEIGHT] = weightString

        FirestoreClass().updateWeight(
            this@UpdateWeightActivity,
            weightHashMap, weightID
        )
    }
}