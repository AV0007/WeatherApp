package com.example.weatherapp.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

/*  //image adapter for loading image with glide and data binding*/

@BindingAdapter("imageFromUrl")
fun ImageView.loadImage(url: String) {
    if (url.isNotEmpty()) {
        Glide.with(this.context).load(url).into(this)
    }

}