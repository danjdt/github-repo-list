package com.danjdt.githubjavarepos.di

import com.danjdt.data.network.GithubRepositoryImpl
import com.danjdt.data.network.datasource.RepositoryDataSource
import com.danjdt.data.network.datasource.RepositoryDataSourceImpl
import com.danjdt.domain.repository.GithubRepository
import kotlinx.coroutines.FlowPreview
import org.koin.dsl.module

/**
 *  @autor danieljdt
 *  @date 2019-11-02
 **/

@FlowPreview
val repositoryModule = module {

    single<RepositoryDataSource> { RepositoryDataSourceImpl(get()) }

    single<GithubRepository> { GithubRepositoryImpl(get()) }
}