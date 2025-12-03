package com.example.mybank.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mybank.feature.auth.LoginScreen
import com.example.mybank.feature.capture.CaptureIdentityScreen
import com.example.mybank.feature.dashboard.DashboardScreen
import com.example.mybank.feature.transactions.TransactionsScreen
import com.example.mybank.feature.transfer.TransferScreen
import com.example.mybank.selfievalidation.navigation.SelfieValidationRoutes
import com.example.mybank.selfievalidation.navigation.selfieValidationGraph

@Composable
fun BankNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = BankRoutes.Login
    ) {
        composable(BankRoutes.Login) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(SelfieValidationRoutes.Capture) {
                        popUpTo(BankRoutes.Login) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(BankRoutes.Dashboard) {
            DashboardScreen(
                onNavigateTransactions = { navController.navigate(BankRoutes.Transactions) },
                onNavigateTransfer = { navController.navigate(BankRoutes.Transfer) },
                onInitiateCapture = { navController.navigate(BankRoutes.CaptureIdentity) }
            )
        }
        composable(BankRoutes.Transactions) {
            TransactionsScreen(onBack = { navController.popBackStack() })
        }
        composable(BankRoutes.Transfer) {
            TransferScreen(onComplete = { navController.popBackStack() })
        }
        selfieValidationGraph(
            navController = navController,
            onBackToLogin = {
                navController.navigate(BankRoutes.Login) {
                    popUpTo(SelfieValidationRoutes.Capture) {
                        inclusive = true
                    }
                }
            },
            onResultContinue = {
                navController.navigate(BankRoutes.Dashboard) {
                    popUpTo(SelfieValidationRoutes.Result) {
                        inclusive = true
                    }
                }
            },
            onResultRedo = {
                navController.navigate(SelfieValidationRoutes.Capture) {
                    popUpTo(SelfieValidationRoutes.Result) {
                        inclusive = true
                    }
                }
            }
        )
        composable(BankRoutes.CaptureIdentity) {
            CaptureIdentityScreen(onBack = { navController.popBackStack() })
        }
    }
}

