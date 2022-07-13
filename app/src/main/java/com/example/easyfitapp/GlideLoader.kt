package com.example.easyfitapp

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.easyfitapp.R
import java.io.IOException

/**
 * A custom object to create a common functions for Glide which can be used in whole application.
 */
class GlideLoader(val context: Context) {

    // TODO Step 3: Create a function to load image from URI for the user profile picture.
    // START
    /**
     * A function to load image from URI for the user profile picture.
     */
    fun loadImage(img: Any, iv: ImageView){
        Glide
            .with(context)
            .load(img)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL) //use this to cache
            .placeholder(R.drawable.propic)
            .into(iv);
    }
}