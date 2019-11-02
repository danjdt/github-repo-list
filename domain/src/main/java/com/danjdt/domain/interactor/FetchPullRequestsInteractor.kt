package com.danjdt.domain.interactor

import com.danjdt.domain.entity.PullRequest
import com.danjdt.domain.interactor.FetchPullRequestsInteractor.Params
import com.danjdt.domain.repository.GithubRepository
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

/**
 *  @autor danieljdt
 *  @date 2019-11-02
 **/
@FlowPreview
class FetchPullRequestsInteractor @Inject constructor(private val repository: GithubRepository) :
    Interactor<List<PullRequest>, Params> {

    override suspend fun execute(params: Params): List<PullRequest> {
        return repository.fetchPullRequests(params.owner, params.repository)
    }

    data class Params(val owner: String, val repository: String)

}