package com.danjdt.data.network

import com.danjdt.data.network.response.RepositoriesReponse
import com.danjdt.domain.entity.PullRequest
import retrofit2.http.Field
import retrofit2.http.GET

/**
 *  @autor danieljdt
 *  @date 2019-11-02
 **/
interface GithubApi {

    @GET("search/repositories?q=language:Java&sort=stars&page={page}")
    suspend fun fetchJavaRepositories(@Field("page") page: Int): RepositoriesReponse

    @GET("repos/{owner}/{repository}/pulls")
    suspend fun fetchPullRequests(@Field("owner") owner: String, @Field("repository") repository: String): List<PullRequest>

}