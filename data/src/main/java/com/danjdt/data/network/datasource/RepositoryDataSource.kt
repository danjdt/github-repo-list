package com.danjdt.data.network.datasource

import com.danjdt.data.network.response.RepositoriesReponse
import com.danjdt.domain.entity.PullRequest
import com.danjdt.domain.entity.Repository
import retrofit2.Call

/**
 *  @autor danieljdt
 *  @date 2019-11-02
 **/
interface RepositoryDataSource {

    suspend fun fetchJavaRepositories(page: Int): List<Repository>

    suspend fun fetchPullRequests(owner: String, repository: String): List<PullRequest>
}