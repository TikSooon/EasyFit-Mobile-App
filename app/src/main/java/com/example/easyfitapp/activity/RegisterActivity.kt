package com.example.easyfitapp.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.easyfitapp.Firestore.FirestoreClass
import com.example.easyfitapp.Model.User
import com.example.easyfitapp.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class RegisterActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val btn_register =  findViewById<Button>(R.id.btnRegister)
        val btn_cancel =  findViewById<TextView>(R.id.btnCancel)

        // START
        btn_register.setOnClickListener {
            registerUser()
        }
        btn_cancel.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Cancel Registration?")
            builder.setMessage("Are you sure to cancel the registration?")
            builder.setPositiveButton(R.string.Yes) { dialog, which ->
                finish()
                Toast.makeText(
                    this@RegisterActivity,
                    resources.getString(R.string.cancel_forget),
                    Toast.LENGTH_SHORT
                ).show()
            }
            builder.setNegativeButton(R.string.No) { dialog, which ->
            }
            builder.show()
        }
    }

    private fun validateRegisterDetails(): Boolean {
        val name =  findViewById<EditText>(R.id.txtName)
        val email =  findViewById<EditText>(R.id.txtEmail2)
        val pass =  findViewById<EditText>(R.id.txtPassword)
        val conPass =  findViewById<EditText>(R.id.txtConPass)
        return when {
            TextUtils.isEmpty(name.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    this@RegisterActivity,
                    resources.getString(R.string.err_msg_enter_name),
                    Toast.LENGTH_SHORT).show()
                    false
            }

            TextUtils.isEmpty(email.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    this@RegisterActivity,
                    resources.getString(R.string.err_msg_enter_email),
                    Toast.LENGTH_SHORT).show()
                false
            }

            TextUtils.isEmpty(pass.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    this@RegisterActivity,
                    resources.getString(R.string.err_msg_enter_password),
                    Toast.LENGTH_SHORT).show()
                false
            }

            TextUtils.isEmpty(conPass.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    this@RegisterActivity,
                    resources.getString(R.string.err_msg_enter_confirm_password),
                    Toast.LENGTH_SHORT).show()
                false
            }

            pass.text.toString().trim { it <= ' ' } != conPass.text.toString()
                .trim { it <= ' ' } -> {
                Toast.makeText(
                    this@RegisterActivity,
                    resources.getString(R.string.err_msg_password_and_confirm_password_mismatch),
                    Toast.LENGTH_SHORT).show()
                false }
            else -> {
                true
            }
        }
    }

    /**
     * A function to register the user with email and password using FirebaseAuth.
     */
    private fun registerUser() {
        val name =  findViewById<EditText>(R.id.txtName)
        val email =  findViewById<EditText>(R.id.txtEmail2)
        val pass =  findViewById<EditText>(R.id.txtPassword)
        // Check with validate function if the entries are valid or not.
        if (validateRegisterDetails()) {

            val mail: String = email.text.toString().trim { it <= ' ' }
            val password: String = pass.text.toString().trim { it <= ' ' }

            // Create an instance and create a register a user with email and password.
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(mail, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->

                        // If the registration is successfully done
                        if (task.isSuccessful) {

                            // Firebase registered user
                            val firebaseUser: FirebaseUser = task.result!!.user!!

                            val user = User(
                                firebaseUser.uid,
                                name.text.toString().trim { it <= ' ' },
                                email.text.toString().trim { it <= ' ' }
                            )

                            FirestoreClass().registerUser(this@RegisterActivity,user)
                            finish()
                        } else {
                            // If the registering is not successful then show error message.
                            //showErrorSnackBar(task.exception!!.message.toString(), true)
                            // Hide the progress dialog
                            Toast.makeText(
                                this@RegisterActivity,
                                task.exception!!.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
        }
    }

    fun userRegistrationSuccess() {

        // TODO Step 5: Replace the success message to the Toast instead of Snackbar.
        Toast.makeText(
            this@RegisterActivity,
            resources.getString(R.string.success_register),
            Toast.LENGTH_SHORT
        ).show()

        /**
         * Here the new user registered is automatically signed-in so we just sign-out the user from firebase
         * and send him to Intro Screen for Sign-In
         */
        FirebaseAuth.getInstance().signOut()
        // Finish the Register Screen
        finish()
    }

}