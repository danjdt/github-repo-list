package com.danjdt.data

import com.danjdt.data.network.GithubRepositoryImpl
import com.danjdt.data.network.datasource.RepositoryDataSource
import com.danjdt.data.network.datasource.RepositoryDataSourceImpl
import com.danjdt.data.utils.assertPullRequests
import com.danjdt.data.utils.assertRepositories
import com.danjdt.domain.repository.GithubRepository
import com.danjdt.domain.utils.collect
import junit.framework.TestCase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.runBlocking

/**
 * @autor danieljdt
 * @date 2019-11-03
 */
@FlowPreview
class GithubRepositoryTest : TestCase() {

    private val githubRepository: GithubRepository by lazy {
        GithubRepositoryImpl(repositoryDataSource)
    }

    private val repositoryDataSource: RepositoryDataSource by lazy {
        RepositoryDataSourceImpl(GithubApiMock())
    }

    private var exception: Exception? = null

    override fun tearDown() {
        exception = null
        super.tearDown()
    }

    fun testValidateFetchJavaRepositoriesSuccessReturnsRepositoriesList() = runBlocking {
        val flow = githubRepository.fetchJavaRepositories(1)
        val list = collect(flow)!!
        assertNotNull(list)
        assertRepositories(list)
    }

    fun testValidateFetchPullRequestsSuccessReturnsPullRequestsList() = runBlocking {
        val flow = githubRepository.fetchPullRequests("owner", "repository", 1)
        val list = collect(flow)!!
        assertNotNull(list)
        assertPullRequests(list)
    }

    fun testValidateFetchJavaRepositoriesSuccessErrorThrowsException() = runBlocking {
        try {
            val flow = githubRepository.fetchJavaRepositories(0)
            collect(flow)!!
        } catch (e: Exception) {
            exception = e
        }

        assertNotNull(exception)
    }

    fun testValidateFetchPullRequestsSuccessErrorThrowsException() = runBlocking {
        try {
            val flow = githubRepository.fetchPullRequests("owner", "repository", 0)
            collect(flow)!!
        } catch (e: Exception) {
            exception = e
        }

        assertNotNull(exception)
    }
}
