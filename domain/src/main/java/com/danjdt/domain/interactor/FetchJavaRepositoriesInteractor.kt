package com.danjdt.domain.interactor

import com.danjdt.domain.entity.Repository
import com.danjdt.domain.interactor.FetchJavaRepositoriesInteractor.Params

/**
 *  @autor danieljdt
 *  @date 2019-11-02
 **/
interface FetchJavaRepositoriesInteractor  : Interactor<List<Repository>, Params> {

    data class Params(val page: Int)
}