package com.danjdt.githubjavarepos.di

import android.content.Context
import com.danjdt.data.network.GithubApi
import com.danjdt.githubjavarepos.BuildConfig
import com.danjdt.githubjavarepos.extensions.hasNetwork
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 *  @autor danieljdt
 *  @date 2019-11-02
 **/

val networkModule = module {
    factory { provideGithubApi(get()) }
    factory { provideHttpClient(get()) }
    single { provideRetrofit(get()) }
}

fun provideRetrofit(client: OkHttpClient): Retrofit {
    val gson = GsonBuilder().setFieldNamingPolicy(
        FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES
    )
        .create()

    return Retrofit.Builder()
        .baseUrl(BuildConfig.API_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

}

fun provideGithubApi(retrofit: Retrofit): GithubApi = retrofit.create(GithubApi::class.java)

fun provideHttpClient(context: Context): OkHttpClient {
    val cacheSize = 5L * 1024 * 1024
    val cache = Cache(context.cacheDir, cacheSize)
    return OkHttpClient.Builder()
        .cache(cache)
        .addInterceptor(createCacheControlInterceptor(context))
        .build()
}

private fun createCacheControlInterceptor(context: Context): Interceptor {
    return Interceptor { chain ->
        var request = chain.request()

        if (context.hasNetwork) {
            request = request.newBuilder().header(
                "Cache-Control",
                "public, only-if-cached"
            ).build()
        }

        val response = chain.proceed(request)
        response.newBuilder()
            .header("Cache-Control", "public, max-age=" + 7 * 60 * 60 * 24)
            .build()
    }
}
