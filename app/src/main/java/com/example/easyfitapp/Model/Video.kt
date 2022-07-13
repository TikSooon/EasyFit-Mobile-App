package com.example.easyfitapp.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Video(
    var id: String = "",
    val thumbnail: String = "",
    val title: String = "",
    val url: String = "",
    val desc:String = ""
): Parcelable
