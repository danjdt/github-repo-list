package com.danjdt.githubjavarepos.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.danjdt.domain.exception.EmptyListException
import com.danjdt.githubjavarepos.mock.DUMMY_REPOSITORIES
import com.danjdt.githubjavarepos.utils.assertRepositories
import com.danjdt.githubjavarepos.mock.FetchJavaRepositoriesInteractorMock
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

/**
 *  @autor danieljdt
 *  @date 2019-11-03
 **/
class JavaRepositoriesViewModelTests {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val interactor: FetchJavaRepositoriesInteractorMock by lazy {
        FetchJavaRepositoriesInteractorMock()
    }

    private val viewModel: JavaRepositoriesViewModel by lazy {
        JavaRepositoriesViewModel(interactor)
    }

    @After
    fun tearDown() {
        interactor.exception = null
        interactor.list = DUMMY_REPOSITORIES
    }

    @Test
    fun testValidateViewModelStateOnFetchFirstPageSuccess() = runBlocking {
        viewModel.fetchFirstPage()
        val list = viewModel.repositories.value
        assertNotNull(list)
        assertRepositories(list!!)
    }

    @Test
    fun testValidateViewModelThrowsExceptionOnFetchFirstPageEmpty() = runBlocking {
        interactor.list = ArrayList()
        viewModel.fetchFirstPage()

        val list = viewModel.repositories.value
        assertNull(list)

        val exception = viewModel.exception.value
        assertTrue(exception is EmptyListException)
    }

    @Test
    fun testValidateViewModelStateOnFetchFirstPageFail() = runBlocking {
        val e = Exception()
        interactor.exception = e
        viewModel.fetchFirstPage()

        val exception = viewModel.exception.value
        assertNotNull(exception)
        assertEquals(e, exception)
    }

    @Test
    fun testValidateViewModelStateOnFetchNextPageSuccess() = runBlocking {
        viewModel.fetchFirstPage()
        viewModel.fetchNextPage()
        val list = viewModel.repositories.value
        assertNotNull(list)
        assertEquals(20, list!!.size)
    }

    @Test
    fun testValidateViewModelDoNothingOnFetchNextPageEmpty() = runBlocking {
        viewModel.fetchFirstPage()

        val beforeList = viewModel.repositories.value

        assertNotNull(beforeList)
        assertEquals(10, beforeList!!.size)

        interactor.list = ArrayList()
        viewModel.fetchNextPage()

        val afterList = viewModel.repositories.value

        assertNotNull(afterList)
        assertEquals(10, afterList!!.size)
    }

    @Test
    fun testValidateViewModelStateOnFetchNextPageFail() = runBlocking {
        val e = Exception()
        viewModel.fetchFirstPage()
        interactor.exception = e
        viewModel.fetchNextPage()

        val exception = viewModel.exception.value
        assertNull(exception)
    }

    @Test
    fun testValidateViewModelStateOnRefreshSuccess() = runBlocking {
        viewModel.fetchFirstPage()
        viewModel.fetchNextPage()

        val beforeRefreshList = viewModel.repositories.value
        assertNotNull(beforeRefreshList)
        assertEquals(20, beforeRefreshList!!.size)

        viewModel.refresh()
        val afterRefreshList = viewModel.repositories.value
        assertNotNull(afterRefreshList)
        assertEquals(10, afterRefreshList!!.size)
    }

    @Test
    fun testValidateViewModelStateOnRefreshFail() = runBlocking {
        val e = Exception()
        interactor.exception = e
        viewModel.refresh()

        val exception = viewModel.exception.value
        assertNotNull(exception)
        assertEquals(e, exception)
    }
}