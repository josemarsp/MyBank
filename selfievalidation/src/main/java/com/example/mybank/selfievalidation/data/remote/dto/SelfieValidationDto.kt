package com.example.mybank.selfievalidation.data.remote.dto

import com.squareup.moshi.Json

data class SelfieRequestDto(
    @Json(name = "imageBase64")
    val imageBase64: String
)

data class SelfieResponseDto(
    @Json(name = "status")
    val status: String
)

