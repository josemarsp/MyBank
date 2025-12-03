package com.example.mybank.feature.dashboard

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.example.mybank.core.model.BalanceResponse
import com.example.mybank.core.model.TransactionResponse
import com.example.mybank.core.model.TransactionType
import com.example.mybank.ui.theme.WhiteLabelBrand
import com.example.mybank.ui.theme.WhiteLabelTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class DashboardScreenInstrumentedTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun `action buttons trigger callbacks`() {
        var transactionsPressed = false
        var transferPressed = false
        var capturePressed = false

        composeTestRule.setContent {
            WhiteLabelTheme(brand = WhiteLabelBrand.ClientA) {
                DashboardScreenContent(
                    state = DashboardUiState(
                        balance = BalanceResponse(10.0, "R$", "Atualizado"),
                        transactions = listOf(
                            TransactionResponse(
                                id = "tx-200",
                                date = "2025-12-02",
                                description = "Teste",
                                amount = 50.0,
                                type = TransactionType.CREDIT,
                                category = "PIX"
                            )
                        )
                    ),
                    onNavigateTransactions = { transactionsPressed = true },
                    onNavigateTransfer = { transferPressed = true },
                    onInitiateCapture = { capturePressed = true }
                )
            }
        }

        composeTestRule.onNodeWithTag(DashboardScreenTestTags.TransactionsButton).performClick()
        composeTestRule.onNodeWithTag(DashboardScreenTestTags.TransferButton).performClick()
        composeTestRule.onNodeWithTag(DashboardScreenTestTags.CaptureButton).performClick()

        assertTrue(transactionsPressed)
        assertTrue(transferPressed)
        assertTrue(capturePressed)
    }
}

