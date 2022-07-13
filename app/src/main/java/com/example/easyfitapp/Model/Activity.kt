package com.example.easyfitapp.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Activity(
    var id: String = "",
    var name: String = ""
): Parcelable
