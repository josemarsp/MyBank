package com.example.mybank.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybank.core.model.BalanceResponse
import com.example.mybank.core.model.TransactionResponse
import com.example.mybank.core.usecase.GetBalanceUseCase
import com.example.mybank.core.usecase.GetTransactionsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DashboardUiState(
    val isLoading: Boolean = false,
    val idle: Boolean = true,
    val balance: BalanceResponse? = null,
    val transactions: List<TransactionResponse> = emptyList(),
    val error: String? = null
)

class DashboardViewModel(
    private val balanceUseCase: GetBalanceUseCase,
    private val transactionsUseCase: GetTransactionsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val balanceResult = balanceUseCase.execute()
            val transactionsResult = transactionsUseCase.execute()
            _uiState.update {
                it.copy(
                    isLoading = false,
                    balance = balanceResult.getOrNull(),
                    transactions = transactionsResult.getOrNull() ?: emptyList(),
                    error = balanceResult.exceptionOrNull()?.message
                        ?: transactionsResult.exceptionOrNull()?.message
                )
            }
        }
    }
}

