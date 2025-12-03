package com.example.mybank.di

import com.example.mybank.core.usecase.GetBalanceUseCase
import com.example.mybank.core.usecase.GetTransactionsUseCase
import com.example.mybank.core.usecase.LoginUseCase
import com.example.mybank.core.usecase.TransferUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { LoginUseCase(get()) }
    factory { GetBalanceUseCase(get()) }
    factory { GetTransactionsUseCase(get()) }
    factory { TransferUseCase(get()) }
}

