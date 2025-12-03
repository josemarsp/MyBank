package com.example.mybank.di

import com.example.mybank.selfievalidation.data.remote.SelfieValidationApi
import com.example.mybank.selfievalidation.data.repository.SelfieValidationRepositoryImpl
import com.example.mybank.selfievalidation.data.util.SelfieBase64Converter
import com.example.mybank.selfievalidation.domain.repository.SelfieValidationRepository
import com.example.mybank.selfievalidation.domain.usecase.ValidateSelfieUseCase
import com.example.mybank.selfievalidation.presentation.viewmodel.SelfieValidationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val selfieValidationModule = module {
    single { get<Retrofit>().create(SelfieValidationApi::class.java) }
    single { SelfieBase64Converter() }
    factory<SelfieValidationRepository> {
        SelfieValidationRepositoryImpl(get())
    }
    factory { ValidateSelfieUseCase(get()) }
    viewModel { SelfieValidationViewModel(get(), get()) }
}

