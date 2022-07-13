package com.example.easyfitapp.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.easyfitapp.Firestore.FirestoreClass
import com.example.easyfitapp.Model.User
import com.example.easyfitapp.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // TODO Step 7: Assign a onclick event to the register text to launch the register activity.
        // START
        /*
           tv_register.setOnClickListener {

               // Launch the register screen when the user clicks on the text.
               val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
               startActivity(intent)
           }*/
        //END
        val forget =  findViewById<TextView>(R.id.lblForget)
        val login =  findViewById<Button>(R.id.btnLogin)
        val reg =  findViewById<TextView>(R.id.lblNew)

        progressBar = findViewById(R.id.progressBar)
        // Click event assigned to Forgot Password text.
        forget.setOnClickListener{
            // Launch the forget password screen when the user clicks on the text.
            startActivity(Intent(this@LoginActivity, ForgetPasswordActivity::class.java))
        }
        // Click event assigned to Login button.
        login.setOnClickListener{
            login()
        }
        //Click event assigned to Register text.
        reg.setOnClickListener{
            // Launch the register screen when the user clicks on the text.
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }

    /**
     * A function to validate the login entries of a user.
     */
    private fun validateLoginDetails(): Boolean {
        val email =  findViewById<EditText>(R.id.txtEmail)
        val pass =  findViewById<EditText>(R.id.txtPass)
        return when {
            TextUtils.isEmpty(email.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    this@LoginActivity,
                    resources.getString(R.string.err_msg_enter_email),
                    Toast.LENGTH_SHORT).show()
                false
            }
            TextUtils.isEmpty(pass.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    this@LoginActivity,
                    resources.getString(R.string.err_msg_enter_password),
                    Toast.LENGTH_SHORT).show()
                false
            }
            else -> {
                true
            }
        }
    }

    /**
     * A function to Log-In. The user will be able to log in using the registered email and password with Firebase Authentication.
     */
    private fun login() {
        progressBar.visibility = View.VISIBLE
        val email =  findViewById<EditText>(R.id.txtEmail)
        val pass =  findViewById<EditText>(R.id.txtPass)
        if (validateLoginDetails()) {

            // Get the text from editText and trim the space
            val mail = email.text.toString().trim { it <= ' ' }
            val password = pass.text.toString().trim { it <= ' ' }

            // Log-In using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(mail, password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        // TODO Step 5: Move the hide progress dialog to else part and remove the success message and call the getUserDetails function from Firestore class once the user is logged in.
                        // START
                        /*showErrorSnackBar("You are logged in successfully.", false)*/

                        FirestoreClass().getUser(this@LoginActivity)

                        // END
                    } else {
                        // Hide the progress dialog
                        Toast.makeText(
                            this@LoginActivity,
                            task.exception!!.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    /**
     * A function to notify user that logged in success and get the user details from the FireStore database after authentication.
     */

    fun loginSuccess(user: User){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
