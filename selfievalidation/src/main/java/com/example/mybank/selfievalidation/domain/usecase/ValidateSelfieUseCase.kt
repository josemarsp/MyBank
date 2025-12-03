package com.example.mybank.selfievalidation.domain.usecase

import com.example.mybank.selfievalidation.domain.model.SelfieValidationResult
import com.example.mybank.selfievalidation.domain.repository.SelfieValidationRepository

class ValidateSelfieUseCase(
    private val repository: SelfieValidationRepository
) {
    suspend operator fun invoke(imageBase64: String): SelfieValidationResult {
        return repository.validateSelfie(imageBase64)
    }
}

