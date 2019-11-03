package com.danjdt.githubjavarepos.extensions

import java.text.DecimalFormat

/**
 *  @autor danieljdt
 *  @date 2019-11-02
 **/

fun Int.decimalFormat() : String {
    val decimalFormat = DecimalFormat("#,###")
    return decimalFormat.format(this)
}