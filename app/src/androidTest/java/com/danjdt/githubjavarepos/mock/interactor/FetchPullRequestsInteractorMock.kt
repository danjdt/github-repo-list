package com.danjdt.githubjavarepos.mock.interactor

import com.danjdt.domain.entity.PullRequest
import com.danjdt.domain.interactor.FetchPullRequestsInteractor
import com.danjdt.githubjavarepos.mock.dummy.DUMMY_PULL_REQUESTS

/**
 *  @autor danieljdt
 *  @date 2019-11-03
 **/
class FetchPullRequestsInteractorMock : FetchPullRequestsInteractor {

    var exception: Exception? = null
    var list = DUMMY_PULL_REQUESTS

    override suspend fun execute(params: FetchPullRequestsInteractor.Params): List<PullRequest> {
        exception?.let {
            throw  it
        } ?: return list
    }

}