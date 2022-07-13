package com.example.easyfitapp.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ActivityDetails(
    val user: String = "",
    val activity: String = "",
    val date: String = "",
    val weight: String = "",
    val set: String = "",
    val rep: String = "",
    var id: String = ""): Parcelable