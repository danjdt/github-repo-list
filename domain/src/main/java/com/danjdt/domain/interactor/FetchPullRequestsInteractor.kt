package com.danjdt.domain.interactor

import com.danjdt.domain.entity.PullRequest
import com.danjdt.domain.interactor.FetchPullRequestsInteractor.Params

/**
 *  @autor danieljdt
 *  @date 2019-11-02
 **/
interface FetchPullRequestsInteractor : Interactor<List<PullRequest>, Params> {

    data class Params(val owner: String, val repository: String, val page: Int)
}