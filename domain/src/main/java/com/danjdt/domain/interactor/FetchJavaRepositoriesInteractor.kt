package com.danjdt.domain.interactor

import com.danjdt.domain.entity.Repository
import com.danjdt.domain.interactor.FetchJavaRepositoriesInteractor.Params
import com.danjdt.domain.repository.GithubRepository
import com.danjdt.domain.utils.collect
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

/**
 *  @autor danieljdt
 *  @date 2019-11-02
 **/
@FlowPreview
class FetchJavaRepositoriesInteractor @Inject constructor(private val repository: GithubRepository) :
    Interactor<List<Repository>, Params> {

    override suspend fun execute(params: Params): List<Repository> {
        val response = collect(repository.fetchJavaRepositories(params.page))
        return response ?: ArrayList()
    }

    data class Params(val page: Int)

}