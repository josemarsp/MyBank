package com.example.mybank.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybank.core.model.TransactionResponse
import com.example.mybank.core.usecase.GetTransactionsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TransactionsUiState(
    val isLoading: Boolean = false,
    val transactions: List<TransactionResponse> = emptyList(),
    val error: String? = null
)

class TransactionsViewModel(
    private val getTransactionsUseCase: GetTransactionsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionsUiState())
    val uiState: StateFlow<TransactionsUiState> = _uiState

    init {
        loadTransactions()
    }

    fun loadTransactions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val result = getTransactionsUseCase.execute()
            _uiState.update {
                it.copy(
                    isLoading = false,
                    transactions = result.getOrNull() ?: emptyList(),
                    error = result.exceptionOrNull()?.message
                )
            }
        }
    }
}

