package com.danjdt.githubjavarepos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.danjdt.domain.entity.Repository
import com.danjdt.domain.exception.EmptyListException
import com.danjdt.domain.interactor.FetchJavaRepositoriesInteractor
import com.danjdt.githubjavarepos.extensions.add
import com.danjdt.githubjavarepos.utils.PAGE_LIMIT

/**
 *  @autor danieljdt
 *  @date 2019-11-02
 **/
class JavaRepositoriesViewModel(private val interactor: FetchJavaRepositoriesInteractor) :
    ViewModel() {

    // region Private Properties

    private var page: Int = 1

    private val _repositories: MutableLiveData<List<Repository>> = MutableLiveData()
    val repositories: LiveData<List<Repository>>
        get() = _repositories

    private val _exception: MutableLiveData<Exception> = MutableLiveData()
    val exception: LiveData<Exception>
        get() = _exception

    private val _hasLoadMore: MutableLiveData<Boolean> = MutableLiveData()
    val hasLoadMore: LiveData<Boolean>
        get() = _hasLoadMore

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    // endregion

    // region Public Methods

    suspend fun fetchFirstPage() {
        if (isFirstPage()) {
            showLoading()
            fetchJavaRepositories()
            hideLoading()
        }
    }

    suspend fun fetchNextPage() {
        fetchJavaRepositories()
    }

    suspend fun refresh() {
        resetViewModel()
        fetchFirstPage()
    }

    // endregion

    // region Private Methods

    private fun resetViewModel() {
        page = 1
        _repositories.postValue(null)
    }

    private fun incrementPage() {
        page++
    }

    private fun createParams(): FetchJavaRepositoriesInteractor.Params {
        return FetchJavaRepositoriesInteractor.Params(page)
    }

    private fun isFirstPage(): Boolean {
        return page == 1
    }

    private fun hideLoading() {
        _isLoading.postValue(false)

    }

    private fun showLoading() {
        _isLoading.postValue(true)
    }

    private suspend fun fetchJavaRepositories() {
        try {
            val response = interactor.execute(createParams())

            if (isRepositoriesEmpty() && response.isEmpty()) {
                throw EmptyListException()
            }

            if(response.size >= PAGE_LIMIT) {
                _hasLoadMore.postValue(response.isNotEmpty())
            }

            with(_repositories) {
                postValue(value?.add(response) ?: response)
            }

            incrementPage()

        } catch (e: Exception) {
            handleException(e)
        }
    }

    private fun isRepositoriesEmpty(): Boolean {
        return _repositories.value?.isEmpty() ?: true
    }

    private fun handleException(e: Exception) {
        if (isRepositoriesEmpty()) {
            _exception.postValue(e)
        }
    }

    // endregion
}