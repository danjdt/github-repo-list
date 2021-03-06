package com.danjdt.data.interactor

import com.danjdt.domain.entity.Repository
import com.danjdt.domain.interactor.FetchJavaRepositoriesInteractor
import com.danjdt.domain.DUMMY_REPOSITORIES

/**
 *  @autor danieljdt
 *  @date 2019-11-03
 **/
class FetchJavaRepositoriesInteractorMock : FetchJavaRepositoriesInteractor {

    var exception: Exception? = null
    var list = DUMMY_REPOSITORIES

    override suspend fun execute(params: FetchJavaRepositoriesInteractor.Params): List<Repository> {
        exception?.let {
            throw  it
        } ?: return list
    }

}