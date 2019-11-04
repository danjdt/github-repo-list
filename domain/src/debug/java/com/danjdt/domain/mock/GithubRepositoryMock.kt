package com.danjdt.domain.mock

import com.danjdt.domain.dummyPullRequestFlow
import com.danjdt.domain.dummyRepositoriesFlow
import com.danjdt.domain.entity.PullRequest
import com.danjdt.domain.entity.Repository
import com.danjdt.domain.repository.GithubRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 *  @autor danieljdt
 *  @date 2019-11-03
 **/
@FlowPreview
class GithubRepositoryMock: GithubRepository {

    override suspend fun fetchJavaRepositories(page: Int): Flow<List<Repository>> = flow {
        dummyRepositoriesFlow()
    }

    override suspend fun fetchPullRequests(owner: String, repository: String, page: Int): Flow<List<PullRequest>> = flow {
        dummyPullRequestFlow()
    }
}