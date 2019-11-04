package com.danjdt.githubjavarepos.di

import com.danjdt.domain.entity.Repository
import com.danjdt.domain.interactor.FetchPullRequestsInteractor
import com.danjdt.domain.interactor.FetchPullRequestsInteractorImpl
import com.danjdt.githubjavarepos.ui.pullrequests.PullRequestsViewModel
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 *  @autor danieljdt
 *  @date 2019-11-03
 **/
@FlowPreview
val pullRequestsModule = module {

    single<FetchPullRequestsInteractor> { FetchPullRequestsInteractorImpl(get()) }

    viewModel { (repository: Repository) ->
        PullRequestsViewModel(
            get(),
            repository
        )
    }
}

