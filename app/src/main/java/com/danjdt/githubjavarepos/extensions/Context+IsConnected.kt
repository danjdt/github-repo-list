package com.danjdt.githubjavarepos.extensions

import android.content.Context
import android.net.ConnectivityManager

/**
 *  @autor danieljdt
 *  @date 2019-11-03
 **/

val Context.hasNetwork: Boolean
    get() {
        return (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
            .activeNetworkInfo?.isConnected == true
    }