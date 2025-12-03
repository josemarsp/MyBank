package com.example.mybank.selfievalidation.data.remote

import com.example.mybank.selfievalidation.data.remote.dto.SelfieRequestDto
import com.example.mybank.selfievalidation.data.remote.dto.SelfieResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface SelfieValidationApi {
    @POST("selfie/validate")
    suspend fun validateSelfie(
        @Body request: SelfieRequestDto
    ): SelfieResponseDto
}

