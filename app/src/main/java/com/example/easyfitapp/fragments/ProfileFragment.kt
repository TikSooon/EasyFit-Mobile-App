package com.example.easyfitapp.fragments

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.easyfitapp.Constants
import com.example.easyfitapp.Firestore.FirestoreClass
import com.example.easyfitapp.GlideLoader
import com.example.easyfitapp.R
import com.example.easyfitapp.activity.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import java.io.IOException
import java.util.*


class ProfileFragment : Fragment(), View.OnClickListener {

    //Profile Image Stuff
    private lateinit var profileImage: ImageView
    private lateinit var profileImageAdd: ImageView
    private lateinit var imgUri: Uri
    private lateinit var imageID: String
    private lateinit var progress: ProgressBar

    private lateinit var v: View

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        v = inflater.inflate(R.layout.fragment_profile, container, false)
        v.findViewById<Button>(R.id.btnProfileSignOut).setOnClickListener(this)
        v.findViewById<TextView>(R.id.ProfileName).text = Constants.LOGGED_IN_NAME
        v.findViewById<TextView>(R.id.Email).text = Constants.LOGGED_IN_EMAIL
        profileImage = v.findViewById(R.id.ivProfile)
        profileImageAdd = v.findViewById(R.id.ivProfileAdd)
        profileImageAdd.setOnClickListener(this)
        progress = v.findViewById(R.id.progressBar)
        loadProfileImage()
        return v
    }

    override fun onResume() {
        loadProfileImage()
        super.onResume()
    }

    private fun loadProfileImage(){
        if (Constants.LOGGED_IN_EMAIL.isNotEmpty()){
            GlideLoader(v.context).loadImage(Constants.LOGGED_IN_IMAGE, profileImage)
        }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id){
                R.id.btnProfileSignOut -> {
                    val builder = AlertDialog.Builder(v.context)
                    builder.setTitle("Sign Out")
                    builder.setMessage("Are you sure to sign out?")
                    builder.setPositiveButton(R.string.Yes) { dialog, which ->
                        FirebaseAuth.getInstance().signOut()
                        startActivity(Intent(activity, LoginActivity::class.java))
                        activity?.finish()
                    }
                    builder.setNegativeButton(R.string.No) { dialog, which ->
                    }
                    builder.show()
                }
                R.id.ivProfileAdd -> {
                    val builder = AlertDialog.Builder(v.context)
                    builder.setTitle("Message")
                    builder.setMessage("Do you want to change your profile picture?")
                    builder.setPositiveButton(R.string.Yes) { dialog, which ->
                        pickProfileImage()
                    }
                    builder.setNegativeButton(R.string.No) { dialog, which ->
                    }
                    builder.show()
                }
            }
        }
    }


    private fun pickProfileImage() {
        progress.visibility = View.VISIBLE
        profileImageAdd.visibility = View.GONE
        val intent = Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==1 && resultCode== AppCompatActivity.RESULT_OK && data!=null && data.data!=null){
            imgUri = data.data!!
            profileImage.setImageURI(imgUri)
            imageID = UUID.randomUUID().toString()
            try {
                GlideLoader(requireActivity()).loadImage(imgUri, profileImage)
                addImage()
                Constants.LOGGED_IN_IMAGE = imgUri.toString()
            }
            catch (e: IOException){ Log.e("Glide Error", "Image upload failed:" + e.stackTrace) }
        }
    }

    private fun addImage() { FirestoreClass().addProfileImage(this@ProfileFragment, imgUri, imageID) }

    fun addedImage(imgUri: String) {
        FirestoreClass().addProfileImageToUser(Constants.LOGGED_IN_ID, imgUri)
        loadProfileImage()
        progress.visibility = View.GONE
        profileImageAdd.visibility = View.VISIBLE
        Toast.makeText(activity, "Image Updated!", Toast.LENGTH_SHORT).show()
    }
}