package com.example.mybank.selfievalidation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.mybank.selfievalidation.presentation.ui.CaptureSelfieScreen
import com.example.mybank.selfievalidation.presentation.ui.SelfieResultScreen

fun NavGraphBuilder.selfieValidationGraph(
    navController: NavHostController,
    onBackToLogin: () -> Unit,
    onResultContinue: () -> Unit,
    onResultRedo: () -> Unit
) {
    composable(SelfieValidationRoutes.Capture) {
        CaptureSelfieScreen(
            onBack = onBackToLogin,
            onValidationSuccess = { status ->
                navController.navigate(SelfieValidationRoutes.resultRoute(status)) {
                    popUpTo(SelfieValidationRoutes.Capture) {
                        inclusive = true
                    }
                }
            }
        )
    }

    composable(route = SelfieValidationRoutes.ResultWithArg) { backStackEntry ->
        val status = backStackEntry.arguments?.getString(SelfieValidationRoutes.ResultStatusArg) ?: "APROVADO"
        SelfieResultScreen(
            status = status,
            onContinue = onResultContinue,
            onRedo = onResultRedo
        )
    }
}

