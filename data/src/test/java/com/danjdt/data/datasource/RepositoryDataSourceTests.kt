package com.danjdt.data.datasource

import com.danjdt.data.GithubApiMock
import com.danjdt.data.network.datasource.RepositoryDataSource
import com.danjdt.data.network.datasource.RepositoryDataSourceImpl
import com.danjdt.data.utils.assertPullRequests
import com.danjdt.data.utils.assertRepositories
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
        val list = repositoryDataSource.fetchJavaRepositories(1)
        assertNotNull(list)
        assertRepositories(list)
    }

    fun testRepositoryDataSourceFetchPullRequests() = runBlocking {
        val list = repositoryDataSource.fetchPullRequests("owner", "repository", 1)
        assertNotNull(list)
        assertPullRequests(list)
    }
}