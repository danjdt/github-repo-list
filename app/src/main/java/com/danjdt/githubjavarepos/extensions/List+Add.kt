package com.danjdt.githubjavarepos.extensions

/**
 *  @autor danieljdt
 *  @date 2019-11-02
 **/
fun <T> List<T>.add(other: List<T>) : List<T> {
    val list = ArrayList(this)
    list.addAll(other)
    return list
}