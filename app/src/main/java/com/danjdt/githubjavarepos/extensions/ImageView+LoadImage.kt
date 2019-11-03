package com.danjdt.githubjavarepos.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

/**
 *  @autor danieljdt
 *  @date 2019-11-02
 **/

fun ImageView.loadImageRounded(url: String) {
    Glide.with(context)
        .load(url)
        .apply(RequestOptions.circleCropTransform())
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}
