package com.danjdt.githubjavarepos.di

import com.danjdt.data.network.GithubApi
import com.danjdt.githubjavarepos.BuildConfig
import com.google.gson.GsonBuilder
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 *  @autor danieljdt
 *  @date 2019-11-02
 **/

val networkModule = module {
    factory { provideGithubApi(get()) }
    single { provideRetrofit() }
}

fun provideRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.API_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()

}

fun provideGithubApi(retrofit: Retrofit): GithubApi = retrofit.create(GithubApi::class.java)
