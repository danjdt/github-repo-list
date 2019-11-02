package com.danjdt.data.network

import com.danjdt.data.network.response.RepositoriesReponse
import com.danjdt.domain.entity.PullRequest
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.GET

/**
 *  @autor danieljdt
 *  @date 2019-11-02
 **/
interface GithubApi {

    @GET("search/repositories?q=language:Java&sort=stars&page={page}")
    fun fetchJavaRepositories(@Field("page") page: Int): Observable<RepositoriesReponse>

    @GET("repos/{owner}/{repository}/pulls")
    fun fetchPullRequests(@Field("owner") owner: String, @Field("repository") repository: String): Call<List<PullRequest>>

}