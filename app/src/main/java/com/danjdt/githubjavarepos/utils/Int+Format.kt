package com.danjdt.githubjavarepos.utils

import java.text.DecimalFormat

/**
 *  @autor danieljdt
 *  @date 2019-11-02
 **/

fun Int.format() : String {
    val decimalFormat = DecimalFormat("#,###")
    return decimalFormat.format(this)
}