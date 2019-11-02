package com.danjdt.domain.entity

import java.util.*

/**
 *  @autor danieljdt
 *  @date 2019-11-01
 **/
data class PullRequest(
    val id: Long,
    val createdAt: Date,
    val user: User,
    val title: String,
    val description: String,
    val htmlUrl: String
)