package com.danjdt.githubjavarepos

import android.app.Application
import com.danjdt.githubjavarepos.di.networkModule
import com.danjdt.githubjavarepos.di.repositoriesModule
import com.danjdt.githubjavarepos.di.repositoryModule
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module

/**
 *  @autor danieljdt
 *  @date 2019-11-01
 **/
@FlowPreview
class RepositoryApplication : Application() {

    companion object {
        lateinit var instance: RepositoryApplication
            private set
    }

    private val appModules: List<Module> by lazy {
        listOf(repositoriesModule, repositoryModule, networkModule)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin {
            androidContext(this@RepositoryApplication)
            modules(appModules)
        }
    }
}