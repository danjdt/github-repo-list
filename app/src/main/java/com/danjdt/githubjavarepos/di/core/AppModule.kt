package com.danjdt.githubjavarepos.di.core

import com.danjdt.githubjavarepos.RepositoryApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 *  @autor danieljdt
 *  @date 2019-11-01
 **/
@Module
class AppModule(private val app: RepositoryApplication) {

    @Provides
    @Singleton
    fun provideApp() = app
}