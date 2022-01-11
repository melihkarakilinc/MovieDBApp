package com.melihkarakilinc.movieapp

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide

interface ImageLoader {
    fun imageLoad(context: Context, path: String, imageView: ImageView) {
        Glide
            .with(context)
            .load(ApiUrl.POSTER_PATH + path)
            .centerCrop()
            .placeholder(R.drawable.ic_baseline_add_24)
            .into(imageView);
    }
}