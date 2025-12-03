package com.example.mybank.selfievalidation.domain.repository

import com.example.mybank.selfievalidation.domain.model.SelfieValidationResult

interface SelfieValidationRepository {
    suspend fun validateSelfie(imageBase64: String): SelfieValidationResult
}

