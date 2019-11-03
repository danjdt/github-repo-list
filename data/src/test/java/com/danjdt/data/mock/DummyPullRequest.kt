package com.danjdt.data.mock

import com.danjdt.domain.entity.PullRequest
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 *  @autor danieljdt
 *  @date 2019-11-03
 **/
val DUMMY_PULL_REQUESTS = run {
    val list = mutableListOf<PullRequest>()
    for (i in 1..10) {
        val pullRequest = PullRequest(
            id = 10,
            user = DUMMY_USER,
            title = "Title",
            body = "Lorem ipsum dolor",
            htmlUrl = "https://www.google.com/"
        )

        list.add(pullRequest)
    }
    list
}

@FlowPreview
fun dummyPullRequestFlow(): Flow<List<PullRequest>> = flow {
    emit(DUMMY_PULL_REQUESTS)
}