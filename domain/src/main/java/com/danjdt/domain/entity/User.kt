package com.danjdt.domain.entity

import java.io.Serializable

/**
 *  @autor danieljdt
 *  @date 2019-11-01
 **/
data class User(
    val id: Long,
    val login: String,
    val avatarUrl: String
): Serializable