package com.melihkarakilinc.movieapp.Utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.melihkarakilinc.movieapp.R

class Util {
    fun imageLoader(path: String, imageView: ImageView) {
        Glide
            .with(imageView.context)
            .load(ApiUrl.POSTER_PATH + path)
            .centerCrop()
            .placeholder(R.drawable.ic_baseline_add_24)
            .into(imageView);
    }
}