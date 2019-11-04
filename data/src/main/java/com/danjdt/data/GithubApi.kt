package com.danjdt.data.network

import com.danjdt.data.network.response.RepositoriesReponse
import com.danjdt.domain.entity.PullRequest
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 *  @autor danieljdt
 *  @date 2019-11-02
 **/
interface GithubApi {

    @GET("search/repositories")
    suspend fun fetchRepositories(
        @Query("q") q: String = "language:Java",
        @Query("sort") sort: String = "stars",
        @Query("page") page: Int
    ): RepositoriesReponse

    @GET("repos/{owner}/{repository}/pulls")
    suspend fun fetchPullRequests(
        @Path("owner") owner: String,
        @Path("repository") repository: String,
        @Query("page") page: Int
    ): List<PullRequest>

}