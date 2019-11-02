package com.danjdt.domain.interactor

/**
 *  @autor danieljdt
 *  @date 2019-11-02
 **/
interface Interactor<T, PARAMS> {

    suspend fun execute(params: PARAMS): T

}