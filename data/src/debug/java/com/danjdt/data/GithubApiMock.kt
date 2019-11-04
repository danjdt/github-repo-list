package com.danjdt.data

import com.danjdt.data.mock.DUMMY_REPOSITORIES_RESPONSE
import com.danjdt.data.network.GithubApi
import com.danjdt.data.network.response.RepositoriesReponse
import com.danjdt.domain.DUMMY_PULL_REQUESTS
import com.danjdt.domain.entity.PullRequest

/**
 *  @autor danieljdt
 *  @date 2019-11-03
 **/
class GithubApiMock : GithubApi {

    override suspend fun fetchRepositories(q: String, sort: String, page: Int): RepositoriesReponse {
        if(page > 0) {
            return DUMMY_REPOSITORIES_RESPONSE
        } else {
            throw Exception()
        }
    }

    override suspend fun fetchPullRequests(owner: String, repository: String, page: Int): List<PullRequest> {
        if(page > 0) {
            return DUMMY_PULL_REQUESTS
        } else {
            throw Exception()
        }
    }
}