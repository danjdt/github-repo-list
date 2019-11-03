package com.danjdt.data

import com.danjdt.data.mock.GithubApiMock
import com.danjdt.data.network.GithubRepositoryImpl
import com.danjdt.data.network.datasource.RepositoryDataSource
import com.danjdt.data.network.datasource.RepositoryDataSourceImpl
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
        val response = collect(flow)!!

        for (repository in response) {
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

    fun testValidateFetchPullRequestsSuccessReturnsPullRequestsList() = runBlocking {
        val flow = githubRepository.fetchPullRequests("owner", "repository", 1)
        val response = collect(flow)!!

        for (pullRequest in response) {
            with(pullRequest) {
                assertEquals(10, id)
                assertEquals("Title", title)
                assertEquals("Lorem ipsum dolor", body)
                assertEquals("https://www.google.com/", htmlUrl)
                assertNotNull(user)
            }
        }
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
