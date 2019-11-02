package com.danjdt.githubjavarepos

import android.app.Application
import com.danjdt.githubjavarepos.di.core.AppComponent
import com.danjdt.githubjavarepos.di.core.DaggerAppComponent

/**
 *  @autor danieljdt
 *  @date 2019-11-01
 **/
class RepositoryApplication : Application() {

    // region Companion Object

    companion object {
        lateinit var instance: RepositoryApplication
            private set
    }

    // endregion

    // region Public Properties

    lateinit var applicationComponent: AppComponent
        private set

    // endregion

    // region Constructors

    override fun onCreate() {
        super.onCreate()
        instance = this
        applicationComponent = DaggerAppComponent.create()
    }

    // endregion
}