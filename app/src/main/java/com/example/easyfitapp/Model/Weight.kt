package com.example.easyfitapp.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Weight(
    val user: String = "",
    val weight: String = "",
    val date: String = "",
    var id: String = ""): Parcelable
