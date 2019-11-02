package com.danjdt.domain.repository

import com.danjdt.domain.entity.PullRequest
import com.danjdt.domain.entity.Repository

/**
 *  @autor danieljdt
 *  @date 2019-11-02
 **/
interface GithubRepository {

    fun fetchJavaRepositories(page: Int) : List<Repository>

    fun fetchPullRequests(owner: String, repository: String) : List<PullRequest>
}