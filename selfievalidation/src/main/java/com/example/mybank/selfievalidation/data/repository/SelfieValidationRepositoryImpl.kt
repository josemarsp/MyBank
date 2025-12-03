package com.example.mybank.selfievalidation.data.repository

import com.example.mybank.selfievalidation.data.remote.SelfieValidationApi
import com.example.mybank.selfievalidation.data.remote.dto.SelfieRequestDto
import com.example.mybank.selfievalidation.domain.model.SelfieValidationResult
import com.example.mybank.selfievalidation.domain.model.SelfieValidationStatus
import com.example.mybank.selfievalidation.domain.repository.SelfieValidationRepository

class SelfieValidationRepositoryImpl(
    private val api: SelfieValidationApi
) : SelfieValidationRepository {

    override suspend fun validateSelfie(imageBase64: String): SelfieValidationResult {
        val response = api.validateSelfie(SelfieRequestDto(imageBase64))
        val status = SelfieValidationStatus.values()
            .firstOrNull { it.name == response.status }
            ?: SelfieValidationStatus.APROVADO

        return SelfieValidationResult(status = status)
    }
}

