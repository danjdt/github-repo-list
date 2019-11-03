package com.danjdt.domain.entity

import java.io.Serializable

/**
 *  @autor danieljdt
 *  @date 2019-11-01
 **/
data class PullRequest(
    val id: Long,
    val user: User,
    val title: String,
    val body: String,
    val htmlUrl: String
) : Serializable