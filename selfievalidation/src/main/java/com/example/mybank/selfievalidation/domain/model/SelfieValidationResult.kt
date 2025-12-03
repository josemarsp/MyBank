package com.example.mybank.selfievalidation.domain.model

enum class SelfieValidationStatus {
    APROVADO
}

data class SelfieValidationResult(
    val status: SelfieValidationStatus
)

