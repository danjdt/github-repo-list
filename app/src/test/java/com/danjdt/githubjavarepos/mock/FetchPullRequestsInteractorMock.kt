package com.danjdt.githubjavarepos.mock

import com.danjdt.domain.entity.PullRequest
import com.danjdt.domain.entity.Repository
import com.danjdt.domain.interactor.FetchJavaRepositoriesInteractor
import com.danjdt.domain.interactor.FetchPullRequestsInteractor

/**
 *  @autor danieljdt
 *  @date 2019-11-03
 **/
class FetchPullRequestsInteractorMock : FetchPullRequestsInteractor {

    var exception: Exception? = null

    override suspend fun execute(params: FetchPullRequestsInteractor.Params): List<PullRequest> {
        exception?.let {
            throw  it
        } ?: return DUMMY_PULL_REQUESTS
    }

}