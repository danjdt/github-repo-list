package com.danjdt.data.datasource

import com.danjdt.data.mock.GithubApiMock
import com.danjdt.data.network.datasource.RepositoryDataSource
import com.danjdt.data.network.datasource.RepositoryDataSourceImpl
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking

/**
 *  @autor danieljdt
 *  @date 2019-11-03
 **/
class RepositoryDataSourceTests : TestCase() {

    private val repositoryDataSource: RepositoryDataSource by lazy {
        RepositoryDataSourceImpl(GithubApiMock())
    }

    fun testRepositoryDataSourceFetchRepositories() = runBlocking {
        val response = repositoryDataSource.fetchJavaRepositories(1)

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

    fun testRepositoryDataSourceFetchPullRequests() = runBlocking {
        val response = repositoryDataSource.fetchPullRequests("owner", "repository", 1)
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
}