package com.danjdt.githubjavarepos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.danjdt.domain.entity.Repository
import com.danjdt.domain.interactor.FetchJavaRepositoriesInteractor

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

    suspend fun fetchJavaRepositories() {
        if(isFirstPage()) {
            try {
                incrementPage()
                val response = interactor.execute(createParams())
                _repositories.postValue(response)
            } catch (e : Exception) {
                _exception.postValue(e)
            }
        }
    }

    fun incrementPage() {
        page++
    }

    fun resetPage() {
        page = 1
    }

    private fun createParams(): FetchJavaRepositoriesInteractor.Params {
        return FetchJavaRepositoriesInteractor.Params(page)
    }

    private fun isFirstPage(): Boolean {
        return page == 1
    }
}