package com.valloo.demo.nationalparks.ui.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.valloo.demo.nationalparks.R

fun getProgressDrawable(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }
}

fun ImageView.loadImage(uri: String?, progressDrawable: Drawable) {
    try {
        val options = RequestOptions()
            .placeholder(progressDrawable)
            .error(R.drawable.error)
        Glide.with(this.context)
            .setDefaultRequestOptions(options)
            .load(uri)
            .into(this)
    } catch (t: Throwable) {
        Log.e("ImageView.loadImage", t.message, t)
    }

}
