package com.danjdt.data.network.response

import com.danjdt.domain.entity.Repository

/**
 *  @autor danieljdt
 *  @date 2019-11-02
 **/
data class RepositoriesReponse(
    val items: List<Repository>,
    val incompleteResults: Boolean,
    val totalCounts: Long
)