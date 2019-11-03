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
class FetchJavaRepositoriesInteractorTests : TestCase() {

    private val fetchPullRequestsInteractor by lazy {
        FetchJavaRepositoriesInteractorImpl(GithubRepositoryTestImpl())
    }

    fun testExecuteMethodReturnsPullRequestsList() = runBlocking {
        val params = FetchJavaRepositoriesInteractor.Params(1)
        val list = fetchPullRequestsInteractor.execute(params)

        for (repository in list) {
            with(repository) {
                assertEquals(10, id)
                assertEquals("Name", name)
                assertEquals("Description", description)
                assertEquals(500, forks)
                assertEquals(1000, stargazersCount)
                assertNotNull(owner)
            }
        }
    }
}
