package com.example.easyfitapp.Firestore

import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.easyfitapp.*
import com.example.easyfitapp.Model.*
import com.example.easyfitapp.activity.*
import com.example.easyfitapp.fragments.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage

class FirestoreClass {
    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User){
        mFireStore.collection(Constants.USERS)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(activity, "Registration successful!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(activity, "Error: $e", Toast.LENGTH_SHORT).show()
            }
    }

    fun getUser(activity: LoginActivity){
        val currUser = FirebaseAuth.getInstance().currentUser
        if (currUser != null){
            FirebaseAuth.getInstance().currentUser?.let {
                mFireStore.collection(Constants.USERS)
                    .document(it.uid)
                    .get()
                    .addOnSuccessListener { document ->
                        val user = document.toObject(User::class.java)
                        if (user!=null){
                            Constants.LOGGED_IN_NAME = user.name
                            Constants.LOGGED_IN_ID = user.id
                            Constants.LOGGED_IN_IMAGE = user.image
                            Constants.LOGGED_IN_EMAIL = user.email
                            activity.loginSuccess(user)
                        }
                    }
                    .addOnFailureListener { e -> Log.e("Error", e.toString()) }
            }
        }

    }

    fun getUserInfo(){
        FirebaseAuth.getInstance().currentUser?.uid?.let {
            Log.e("ID is ", it.toString())
            mFireStore.collection(Constants.USERS)
                .document(it)
                .get()
                .addOnSuccessListener { document ->
                    val user = document.toObject(User::class.java)
                    if (user!=null){
                        Log.e("user", user.toString())
                        Constants.LOGGED_IN_NAME = user.name
                        Constants.LOGGED_IN_ID = user.id
                        Constants.LOGGED_IN_IMAGE = user.image
                        Constants.LOGGED_IN_EMAIL = user.email
                    } else { Log.e("Uhh", "nope") }
                }
                .addOnFailureListener { e ->
                    Log.e("Error", e.toString())
                }
        }
    }

    fun getUserID(): String{
        return FirebaseAuth.getInstance().currentUser?.uid.toString()
    }


    fun addProfileImage(fragment: ProfileFragment, uri: Uri?, imgID: String){
        val imgRef = FirebaseStorage.getInstance().reference.child("images/$imgID")
        imgRef.putFile(uri!!)
            .addOnSuccessListener { snapshot ->
                snapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        run {
                            fragment.addedImage(uri.toString())
                            Constants.LOGGED_IN_IMAGE = uri.toString()
                        }
                    }
            }
            .addOnFailureListener{ Log.e("Image upload", "Failed") }
    }

    fun addProfileImageToUser(userID: String, imageID: String){
        val hashmap = HashMap<String, Any>()
        hashmap["image"] = imageID
        mFireStore.collection(Constants.USERS)
            .document(userID)
            .update(hashmap)
            .addOnSuccessListener { Log.e("Img", "Updated") }
            .addOnFailureListener{ Log.e("Img", "Didnt update") }
    }

    fun addWeight(activity: LogWeightActivity, weightInfo: Weight){
        mFireStore.collection(Constants.WEIGHT)
            .document(weightInfo.id)
            .set(weightInfo, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(activity, "Weight added!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(activity, "Error: $e", Toast.LENGTH_SHORT).show()
            }
    }

    fun getWeight(fragment: WeightFragment, userID: String){
        val weightList: ArrayList<Weight> = ArrayList()
        mFireStore.collection(Constants.WEIGHT)
            .whereEqualTo("user", userID)
            .get()
            .addOnSuccessListener { document ->
                for (i in document.documents) {
                    val weight = i.toObject(Weight::class.java)
                    if (weight != null) {
                        weight.id = i.id
                        weightList.add(weight)
                    }
                }
                fragment.fetchedWeight(weightList)
            }
    }

    fun getWeightDetails(context: UpdateWeightActivity, weightID: String){
        mFireStore.collection(Constants.WEIGHT)
            .document(weightID)
            .get()
            .addOnSuccessListener { document ->
                context.gotWeightDetails(document.toObject(Weight::class.java)!!)
            }
            .addOnFailureListener{ exception ->
                Log.e("Error", "Failed to fetch weight: " + exception.message)
            }
    }

    fun updateWeight(activity: UpdateWeightActivity, weightHashMap: HashMap<String, Any>, weightID: String) {
        mFireStore.collection(Constants.WEIGHT)
            // Document ID against which the data to be updated. Here the document id is the current logged in user id.
            .document(weightID)
            // A HashMap of fields which are to be updated.
            .update(weightHashMap)
            .addOnSuccessListener {
                Toast.makeText(activity, "Weight updated!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(activity, "Error: $e", Toast.LENGTH_SHORT).show()
            }
    }

    fun getActivity(fragment: ActivityFragment){
        mFireStore.collection(Constants.ACTIVITY)
            .get()
            .addOnSuccessListener { document ->
                val activityList: ArrayList<Activity> = ArrayList()
                for (i in document.documents){
                    val activity = i.toObject(Activity::class.java)
                    if (activity != null) {
                        activity.id = i.id
                        activityList.add(activity)
                    }
                }
                fragment.fetchedActivity(activityList)
            }
    }

    fun addActivityDetails(activity: LogActivity, activityDetails: ActivityDetails){
        mFireStore.collection(Constants.ACTIVITYDETAIL)
            .document(activityDetails.id)
            .set(activityDetails, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(activity, "Activity added!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(activity, "Error: $e", Toast.LENGTH_SHORT).show()
            }
    }

    fun getActivityDetails(fragment: ActivityDetailsFragment, userID: String, activityID: String){
        val activityList: ArrayList<ActivityDetails> = ArrayList()
        mFireStore.collection(Constants.ACTIVITYDETAIL)
            .whereEqualTo("activity",activityID)
            .whereEqualTo("user", userID)
            .get()
            .addOnSuccessListener { document ->
                for (i in document.documents) {
                    val activity = i.toObject(ActivityDetails::class.java)
                    if (activity != null) {
                        activity.id = i.id
                        activityList.add(activity)
                    }
                }
                fragment.fetchedActivityDetails(activityList)
            }
    }

    fun getUpdateActivityDetails(context: UpdateActivity, activityID: String){
        mFireStore.collection(Constants.ACTIVITYDETAIL)
            .document(activityID)
            .get()
            .addOnSuccessListener { document ->
                context.gotUpdateActivityDetails(document.toObject(ActivityDetails::class.java)!!)
            }
            .addOnFailureListener{ exception ->
                Log.e("Error", "Failed to fetch weight: " + exception.message)
            }
    }

    fun updateActivity(activity: UpdateActivity, activityHashMap: HashMap<String, Any>, activityID: String) {
        mFireStore.collection(Constants.ACTIVITYDETAIL)
            .document(activityID)
            .update(activityHashMap)
            .addOnSuccessListener {
                Toast.makeText(activity, "Acitvity updated!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(activity, "Error: $e", Toast.LENGTH_SHORT).show()
            }
    }

    fun getVideo(fragment: VideoFragment){
        mFireStore.collection(Constants.VIDEO)
            .get()
            .addOnSuccessListener { document ->
                val videoList: ArrayList<Video> = ArrayList()
                for (i in document.documents){
                    val video = i.toObject(Video::class.java)
                    if (video != null) {
                        video.id = i.id
                        videoList.add(video)
                    }
                }
                fragment.fetchedVideo(videoList)
            }
    }
}
