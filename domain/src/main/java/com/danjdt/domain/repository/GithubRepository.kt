package com.danjdt.domain.repository

import com.danjdt.domain.entity.PullRequest
import com.danjdt.domain.entity.Repository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

/**
 *  @autor danieljdt
 *  @date 2019-11-02
 **/
@FlowPreview
interface GithubRepository {

    suspend fun fetchJavaRepositories(page: Int): Flow<List<Repository>>

    suspend fun fetchPullRequests(owner: String, repository: String): Flow<List<PullRequest>>
}