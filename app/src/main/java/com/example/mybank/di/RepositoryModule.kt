package com.example.mybank.di

import com.example.mybank.data.repository.BankRepository
import com.example.mybank.data.repository.RealBankRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<BankRepository> {
        RealBankRepository(
            bankApi = get(),
            secureStorage = get(),
            transactionDao = get()
        )
    }
}

