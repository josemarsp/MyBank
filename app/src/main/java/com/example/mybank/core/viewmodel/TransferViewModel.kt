package com.example.mybank.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybank.core.model.TransferRequest
import com.example.mybank.core.model.TransferResponse
import com.example.mybank.core.usecase.TransferUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TransferUiState(
    val isLoading: Boolean = false,
    val success: String? = null,
    val error: String? = null
)

class TransferViewModel(
    private val transferUseCase: TransferUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransferUiState())
    val uiState: StateFlow<TransferUiState> = _uiState

    fun submitTransfer(toAccount: String, amount: Double, message: String?) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, success = null, error = null) }
            val request = TransferRequest(
                fromAccount = "1234-5",
                toAccount = toAccount,
                amount = amount,
                message = message
            )
            val result = transferUseCase.execute(request)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    success = result.getOrNull()?.transferId,
                    error = result.exceptionOrNull()?.message
                )
            }
        }
    }
}

