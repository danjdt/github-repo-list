package com.danjdt.domain.interactor

import com.danjdt.domain.entity.Repository
import com.danjdt.domain.interactor.FetchJavaRepositoriesInteractor.*
import com.danjdt.domain.repository.GithubRepository
import javax.inject.Inject

/**
 *  @autor danieljdt
 *  @date 2019-11-02
 **/
class FetchJavaRepositoriesInteractor @Inject constructor(private val repository: GithubRepository) :
    Interactor<List<Repository>, Params> {

    override suspend fun execute(params: Params): List<Repository> {
        return repository.fetchJavaRepositories(params.page)
    }

    data class Params(val page: Int)

}