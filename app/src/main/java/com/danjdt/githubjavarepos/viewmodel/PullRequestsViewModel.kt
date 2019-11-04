package com.danjdt.githubjavarepos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.danjdt.domain.entity.PullRequest
import com.danjdt.domain.entity.Repository
import com.danjdt.domain.exception.EmptyListException
import com.danjdt.domain.interactor.FetchPullRequestsInteractor
import com.danjdt.githubjavarepos.extensions.add
import com.danjdt.githubjavarepos.utils.PAGE_LIMIT

/**
 * @autor danieljdt
 * @date 2019-11-03
 */
class PullRequestsViewModel(
    private val interactor: FetchPullRequestsInteractor,
    private val repository: Repository
) : ViewModel() {

    private var page: Int = 1

    private val _pullRequests: MutableLiveData<List<PullRequest>> = MutableLiveData()
    val pullRequests: LiveData<List<PullRequest>>
        get() = _pullRequests

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
            fetchPullRequests()
            hideLoading()
        }
    }

    suspend fun fetchNextPage() {
        fetchPullRequests()
    }

    suspend fun refresh() {
        resetViewModel()
        fetchFirstPage()
    }

    private fun resetViewModel() {
        page = 1
        _pullRequests.postValue(null)
    }

    private fun incrementPage() {
        page++
    }

    private fun createParams(): FetchPullRequestsInteractor.Params {
        return FetchPullRequestsInteractor.Params(repository.owner.login, repository.name, page)
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

    private suspend fun fetchPullRequests() {
        try {
            val response = interactor.execute(createParams())

            if (isPullRequestsEmpty() && response.isEmpty()) {
                throw EmptyListException()
            }

            if(response.size >= PAGE_LIMIT) {
                _hasLoadMore.postValue(response.isNotEmpty())
            }

            with(_pullRequests) {
                postValue(value?.add(response) ?: response)
            }

            incrementPage()

        } catch (e: Exception) {
            handleException(e)
        }
    }

    private fun isPullRequestsEmpty(): Boolean {
        return _pullRequests.value?.isEmpty() ?: true
    }

    private fun handleException(e: Exception) {
        if (isPullRequestsEmpty()) {
            _exception.postValue(e)
        }
    }

}
