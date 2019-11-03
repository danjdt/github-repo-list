package com.danjdt.domain.interactor

import com.danjdt.domain.GithubRepositoryTestImpl
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
        FetchPullRequestsInteractorImpl(GithubRepositoryTestImpl())
    }

    fun testExecuteMethodReturnsPullRequestsList() = runBlocking {
        val params = FetchPullRequestsInteractor.Params("owner", "repository", 1)
        val list = fetchPullRequestsInteractor.execute(params)

        for (pullRequest in list) {
            with(pullRequest) {
                assertEquals(10, id)
                assertEquals("Title", title)
                assertEquals("Lorem ipsum dolor", body)
                assertEquals("https://www.google.com/", htmlUrl)
                assertNotNull(user)
            }
        }
    }
}
