package com.example.mybank.selfievalidation.presentation.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybank.selfievalidation.data.util.SelfieBase64Converter
import com.example.mybank.selfievalidation.domain.model.SelfieValidationStatus
import com.example.mybank.selfievalidation.domain.usecase.ValidateSelfieUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SelfieValidationViewModel(
    private val validateSelfieUseCase: ValidateSelfieUseCase,
    private val selfieBase64Converter: SelfieBase64Converter
) : ViewModel() {

    private val _uiState = MutableStateFlow(SelfieValidationUiState())
    val uiState: StateFlow<SelfieValidationUiState> = _uiState.asStateFlow()

    fun onSelfieCaptured(bitmap: Bitmap) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    selfieBitmap = bitmap
                )
            }

            try {
                val base64 = selfieBase64Converter.bitmapToBase64(bitmap)
                val result = validateSelfieUseCase(base64)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        status = result.status,
                        error = null
                    )
                }
            } catch (throwable: Throwable) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = throwable.message ?: "Erro ao validar selfie"
                    )
                }
            }
        }
    }

    fun clearStatus() {
        _uiState.update {
            it.copy(status = null, error = null)
        }
    }
}

data class SelfieValidationUiState(
    val isLoading: Boolean = false,
    val selfieBitmap: Bitmap? = null,
    val status: SelfieValidationStatus? = null,
    val error: String? = null
)

