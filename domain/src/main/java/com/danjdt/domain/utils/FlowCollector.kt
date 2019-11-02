package com.danjdt.domain.utils

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

/**
 *  @autor danieljdt
 *  @date 2019-11-02
 **/
@FlowPreview
suspend fun <T> collect(flow : Flow<T>) : T? {
    var t : T? = null
    flow.collect {
        t = it
    }

    return t
}