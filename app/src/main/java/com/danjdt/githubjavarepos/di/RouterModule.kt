package com.danjdt.githubjavarepos.di

import android.content.Context
import com.danjdt.githubjavarepos.navigation.Router
import com.danjdt.githubjavarepos.navigation.RouterImpl
import org.koin.dsl.module

/**
 *  @autor danieljdt
 *  @date 2019-11-04
 **/

val routerModule = module {

    single<Router> { (context : Context) -> RouterImpl(context) }
}
