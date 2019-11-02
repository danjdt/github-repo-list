package com.danjdt.domain.interactor

import com.danjdt.domain.entity.PullRequest
import com.danjdt.domain.interactor.FetchPullRequestsInteractor.Params
import com.danjdt.domain.repository.GithubRepository
import com.danjdt.domain.utils.collect
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject


/**
 *  @autor danieljdt
 *  @date 2019-11-02
 **/
@FlowPreview
class FetchPullRequestsInteractor @Inject constructor(private val repository: GithubRepository) :
    Interactor<List<PullRequest>?, Params> {

    override suspend fun execute(params: Params): List<PullRequest>? {
        val response = collect(repository.fetchPullRequests(params.owner, params.repository, params.page))
        return response ?: ArrayList()
    }

    data class Params(val owner: String, val repository: String, val page: Int)
}