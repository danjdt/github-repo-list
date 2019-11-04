package com.danjdt.data.network.datasource

import com.danjdt.domain.entity.PullRequest
import com.danjdt.domain.entity.Repository

/**
 *  @autor danieljdt
 *  @date 2019-11-02
 **/
interface RepositoryDataSource {

    suspend fun fetchJavaRepositories(page: Int): List<Repository>

    suspend fun fetchPullRequests(owner: String, repository: String, page: Int): List<PullRequest>
}