package com.example.mybank.di

import com.example.mybank.core.viewmodel.AuthViewModel
import com.example.mybank.core.viewmodel.DashboardViewModel
import com.example.mybank.core.viewmodel.TransactionsViewModel
import com.example.mybank.core.viewmodel.TransferViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { AuthViewModel(get()) } // MockSecureStorage
    viewModel { DashboardViewModel(get(), get()) }
    viewModel { TransactionsViewModel(get()) }
    viewModel { TransferViewModel(get()) }
}

