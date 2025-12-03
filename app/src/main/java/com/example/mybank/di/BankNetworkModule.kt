package com.example.mybank.di

import android.util.Log
import com.example.mybank.BuildConfig
import com.example.mybank.core.network.AuthorizationInterceptor
import com.example.mybank.data.local.MockSecureStorage
import com.example.mybank.data.remote.BankApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

data class NetworkConfig(val baseUrl: String)

val networkModule = module {
    single { MockSecureStorage() }
    single { NetworkConfig(baseUrl = BuildConfig.BASE_URL) }
    single { AuthorizationInterceptor(get()) }
    single { provideOkHttpClient(get()) }
    single { provideRetrofit(get(), get()) }
    single { get<Retrofit>().create(BankApi::class.java) }
}

private fun provideOkHttpClient(interceptor: AuthorizationInterceptor): OkHttpClient {
    val logging = HttpLoggingInterceptor { message -> Log.v("BankNet", message) }
    logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
    return OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .addInterceptor(logging)
        .callTimeout(35, TimeUnit.SECONDS)
        .connectTimeout(15, TimeUnit.SECONDS)
        .build()
}

private fun provideRetrofit(config: NetworkConfig, client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(config.baseUrl)
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(createMoshi()))
        .build()
}

private fun createMoshi() = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()

