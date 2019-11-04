package com.danjdt.domain.interactor

import com.danjdt.domain.mock.GithubRepositoryMock
import com.danjdt.domain.utils.assertRepositories
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
        FetchJavaRepositoriesInteractorImpl(GithubRepositoryMock())
    }

    fun testExecuteMethodReturnsPullRequestsList() = runBlocking {
        val params = FetchJavaRepositoriesInteractor.Params(1)
        val list = fetchPullRequestsInteractor.execute(params)

        assertNotNull(list)
        assertRepositories(list)
    }
}
