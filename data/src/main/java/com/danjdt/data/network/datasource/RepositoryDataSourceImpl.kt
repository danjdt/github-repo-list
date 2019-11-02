package com.danjdt.data.network.datasource

import com.danjdt.data.network.GithubApi
import com.danjdt.domain.entity.PullRequest
import com.danjdt.domain.entity.Repository
import javax.inject.Inject

/**
 *  @autor danieljdt
 *  @date 2019-11-02
 **/
class RepositoryDataSourceImpl @Inject constructor(
    private val githubApi: GithubApi
) : RepositoryDataSource {

    override suspend fun fetchJavaRepositories(page: Int): List<Repository> {
        val reponse = githubApi.fetchRepositories(page = page)
        return reponse.items
    }

    override suspend fun fetchPullRequests(
        owner: String,
        repository: String,
        page: Int
    ): List<PullRequest> {
        return githubApi.fetchPullRequests(owner, repository, page)
    }
}