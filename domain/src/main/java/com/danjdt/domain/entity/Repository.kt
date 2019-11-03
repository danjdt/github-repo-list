package com.danjdt.domain.entity

import java.io.Serializable
import java.util.*

/**
 *  @autor danieljdt
 *  @date 2019-11-01
 **/
data class Repository(
    val id: Long,
    val name: String,
    val owner: User,
    val description: String,
    val language: String,
    val forks: Int,
    val stargazersCount: Int,
    val updatedAt: Date
) : Serializable