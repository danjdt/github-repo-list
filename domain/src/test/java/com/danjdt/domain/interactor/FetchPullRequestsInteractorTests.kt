package com.danjdt.domain.interactor

import com.danjdt.domain.mock.GithubRepositoryMock
import com.danjdt.domain.utils.assertPullRequests
import junit.framework.TestCase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.runBlocking

/**
 *  @autor danieljdt
 *  @date 2019-11-03
 **/
@FlowPreview
class FetchPullRequestsInteractorTests : TestCase() {

    private val fetchPullRequestsInteractor by lazy {
        FetchPullRequestsInteractorImpl(GithubRepositoryMock())
    }

    fun testExecuteMethodReturnsPullRequestsList() = runBlocking {
        val params = FetchPullRequestsInteractor.Params("owner", "repository", 1)
        val list = fetchPullRequestsInteractor.execute(params)


        assertNotNull(list)
        assertPullRequests(list)
    }
}
