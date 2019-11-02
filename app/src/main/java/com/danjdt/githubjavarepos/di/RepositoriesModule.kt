package com.danjdt.githubjavarepos.di

import com.danjdt.domain.interactor.FetchJavaRepositoriesInteractor
import com.danjdt.domain.interactor.FetchJavaRepositoriesInteractorImpl
import com.danjdt.githubjavarepos.viewmodel.JavaRepositoriesViewModel
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 *  @autor danieljdt
 *  @date 2019-11-02
 **/
@FlowPreview
val repositoriesModule = module {

    single<FetchJavaRepositoriesInteractor> { FetchJavaRepositoriesInteractorImpl(get()) }

    viewModel { JavaRepositoriesViewModel(get()) }
}
