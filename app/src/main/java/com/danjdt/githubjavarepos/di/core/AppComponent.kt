package com.danjdt.githubjavarepos.di.core

import com.danjdt.githubjavarepos.RepositoryApplication
import dagger.Component
import javax.inject.Singleton

/**
 *  @autor danieljdt
 *  @date 2019-11-01
 **/
@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(app: RepositoryApplication)

}