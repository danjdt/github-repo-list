package com.danjdt.domain.interactor

import com.danjdt.domain.entity.PullRequest
import com.danjdt.domain.repository.GithubRepository
import com.danjdt.domain.utils.collect
import kotlinx.coroutines.FlowPreview

/**
 *  @autor danieljdt
 *  @date 2019-11-02
 **/
@FlowPreview
class FetchPullRequestsInteractorImpl(private val repository: GithubRepository) :
    FetchPullRequestsInteractor {

    override suspend fun execute(params: FetchPullRequestsInteractor.Params): List<PullRequest> {
        val flow = repository.fetchPullRequests(params.owner, params.repository, params.page)
        val response = collect(flow)
        return response ?: ArrayList()
    }
}