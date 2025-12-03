package com.example.mybank.selfievalidation.navigation

object SelfieValidationRoutes {
    const val Capture = "selfie_capture"
    const val Result = "selfie_result"
    const val ResultStatusArg = "status"
    const val ResultWithArg = "$Result/{$ResultStatusArg}"

    fun resultRoute(status: String) = "$Result/$status"
}

