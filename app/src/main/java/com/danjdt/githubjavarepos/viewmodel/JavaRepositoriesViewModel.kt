package com.danjdt.githubjavarepos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.danjdt.domain.entity.Repository
import com.danjdt.domain.interactor.FetchJavaRepositoriesInteractor
import com.danjdt.githubjavarepos.extensions.add

/**
 *  @autor danieljdt
 *  @date 2019-11-02
 **/
class JavaRepositoriesViewModel(private val interactor: FetchJavaRepositoriesInteractor) :
    ViewModel() {

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
            with(_repositories) {
                postValue(value?.add(response) ?: response)
            }

            _hasLoadMore.postValue(response.isNotEmpty())
            incrementPage()
        } catch (e: Exception) {
            _exception.postValue(e)
        }
    }
}