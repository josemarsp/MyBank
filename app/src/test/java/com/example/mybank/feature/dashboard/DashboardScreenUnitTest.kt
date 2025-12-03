package com.example.mybank.feature.dashboard

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.example.mybank.core.model.BalanceResponse
import com.example.mybank.core.model.TransactionResponse
import com.example.mybank.core.model.TransactionType
import com.example.mybank.core.viewmodel.DashboardUiState
import com.example.mybank.ui.theme.WhiteLabelBrand
import com.example.mybank.ui.theme.WhiteLabelTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class DashboardScreenUnitTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun render(state: DashboardUiState) {
        composeTestRule.setContent {
            WhiteLabelTheme(brand = WhiteLabelBrand.ClientA) {
                DashboardScreenContent(
                    state = state,
                    onNavigateTransactions = {},
                    onNavigateTransfer = {},
                    onInitiateCapture = {}
                )
            }
        }
    }

    @Test
    fun `balance card shows formatted balance when available`() {
        render(
            DashboardUiState(
                balance = BalanceResponse(999.5, "R$", "Atualizado")
            )
        )

        composeTestRule.onNodeWithText("R$ 999.50").assertIsDisplayed()
    }

    @Test
    fun `balance card shows placeholder when balance missing`() {
        render(DashboardUiState())

        composeTestRule.onNodeWithText("Carregando...").assertIsDisplayed()
    }

    @Test
    fun `shows loading indicator while loading`() {
        render(DashboardUiState(isLoading = true))

        composeTestRule
            .onNodeWithTag(DashboardScreenTestTags.LoadingIndicator)
            .assertIsDisplayed()
    }

    @Test
    fun `shows transactions when available`() {
        val transactions = listOf(
            TransactionResponse(
                id = "tx-100",
                date = "2025-12-01",
                description = "Pix recebido",
                amount = 200.0,
                type = TransactionType.CREDIT,
                category = "PIX"
            )
        )

        render(
            DashboardUiState(
                transactions = transactions
            )
        )

        composeTestRule.onNodeWithText("Pix recebido").assertIsDisplayed()
    }

    @Test
    fun `shows error message when present`() {
        render(DashboardUiState(error = "Falha ao carregar"))

        composeTestRule
            .onNodeWithTag(DashboardScreenTestTags.ErrorText)
            .assertIsDisplayed()
    }
}

