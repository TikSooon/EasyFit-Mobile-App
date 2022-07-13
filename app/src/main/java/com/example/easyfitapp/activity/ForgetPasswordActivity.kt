package com.example.easyfitapp.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.easyfitapp.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class ForgetPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget)

        val btn_reset = findViewById<Button>(R.id.btnReset)
        val btn_cancel = findViewById<TextView>(R.id.btnCancelF)

        btn_reset.setOnClickListener{
            if (validate()) {
                resetPassword()
            }
        }
        btn_cancel.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Message")
            builder.setMessage("Do you want to cancel this operation?")
            builder.setPositiveButton(R.string.Yes) { dialog, which ->
                finish()
                Toast.makeText(
                    this@ForgetPasswordActivity,
                    resources.getString(R.string.cancel_forget),
                    Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton(R.string.No) { dialog, which ->
            }
            builder.show()
        }
    }

    private fun resetPassword() {
        val mail: String = findViewById<EditText>(R.id.txtEmail).text.toString().trim { it <= ' ' }
        FirebaseAuth.getInstance().sendPasswordResetEmail(mail)
            .addOnCompleteListener(
                OnCompleteListener<Void> { task ->

                    // If the registration is successfully done
                    if (task.isSuccessful) {

                        Toast.makeText(
                            this@ForgetPasswordActivity,
                            resources.getString(R.string.email_sent),
                            Toast.LENGTH_SHORT
                        ).show()

                        startActivity(Intent(this@ForgetPasswordActivity, LoginActivity::class.java))
                        finish()
                    } else {
                        // If the registering is not successful then show error message.
                        //showErrorSnackBar(task.exception!!.message.toString(), true)
                        // Hide the progress dialog
                        Toast.makeText(
                            this@ForgetPasswordActivity,
                            task.exception!!.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
    }
    private fun validate(): Boolean {
        val email =  findViewById<EditText>(R.id.txtEmail)

        return when {
            TextUtils.isEmpty(email.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    this@ForgetPasswordActivity,
                    resources.getString(R.string.err_msg_enter_email),
                    Toast.LENGTH_SHORT).show()
                false
            }
            else -> {
                true
            }
        }
    }

}