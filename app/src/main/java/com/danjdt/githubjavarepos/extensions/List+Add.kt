package com.danjdt.githubjavarepos.extensions

import com.danjdt.domain.entity.Repository

/**
 *  @autor danieljdt
 *  @date 2019-11-02
 **/
fun List<Repository>.add(other: List<Repository>) : List<Repository> {
    val list = ArrayList(this)
    list.addAll(other)
    return list
}