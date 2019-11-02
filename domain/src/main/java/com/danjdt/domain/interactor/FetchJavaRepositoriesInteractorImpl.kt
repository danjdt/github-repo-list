package com.danjdt.domain.interactor

import com.danjdt.domain.entity.Repository
import com.danjdt.domain.repository.GithubRepository
import com.danjdt.domain.utils.collect
import kotlinx.coroutines.FlowPreview

/**
 *  @autor danieljdt
 *  @date 2019-11-02
 **/
@FlowPreview
class FetchJavaRepositoriesInteractorImpl(private val repository: GithubRepository) :
    FetchJavaRepositoriesInteractor {

    override suspend fun execute(params: FetchJavaRepositoriesInteractor.Params): List<Repository> {
        val flow = repository.fetchJavaRepositories(params.page)
        val response = collect(flow)
        return response ?: ArrayList()
    }
}