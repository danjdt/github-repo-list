package com.danjdt.data.network

import com.danjdt.data.network.datasource.RepositoryDataSource
import com.danjdt.domain.entity.PullRequest
import com.danjdt.domain.entity.Repository
import com.danjdt.domain.repository.GithubRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 *  @autor danieljdt
 *  @date 2019-11-02
 **/
@FlowPreview
class GithubRepositoryImpl @Inject constructor(private val dataSource: RepositoryDataSource): GithubRepository {

    override suspend fun fetchJavaRepositories(page: Int): Flow<List<Repository>> = flow {
        try {
            val repositories = dataSource.fetchJavaRepositories(page)
            emit(repositories)
        } catch (e : Exception) {
            throw e
        }
    }

    override suspend fun fetchPullRequests(owner: String, repository: String): Flow<List<PullRequest>> = flow {
        try {
            val pullRequests = dataSource.fetchPullRequests(owner, repository)
            emit(pullRequests)
        } catch (e : Exception) {
            throw e
        }
    }

}