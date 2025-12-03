package com.example.mybank.feature.auth

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.example.mybank.core.viewmodel.AuthUiState
import com.example.mybank.ui.theme.WhiteLabelBrand
import com.example.mybank.ui.theme.WhiteLabelTheme
import org.junit.Rule
import org.junit.Test

class LoginScreenInstrumentedTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private fun loadLoginContent() {
        composeTestRule.setContent {
            WhiteLabelTheme(brand = WhiteLabelBrand.ClientA) {
        LoginContent(uiState = AuthUiState.Idle, onLogin = { _, _ -> })
            }
        }
    }

    @Test
    fun loginButton_disabled_for_default_inputs() {
        loadLoginContent()
        composeTestRule.onNodeWithText("Entrar").assertIsNotEnabled()
    }

    @Test
    fun loginButton_enables_with_valid_inputs() {
        loadLoginContent()

        val emailField = composeTestRule.onNodeWithText("Email")
        emailField.performTextClearance()
        emailField.performTextInput("user@example.com")

        val passwordField = composeTestRule.onNodeWithText("Senha")
        passwordField.performTextClearance()
        passwordField.performTextInput("senha1234")

        composeTestRule.onNodeWithText("Entrar").assertIsEnabled()
    }

    @Test
    fun loginButton_stays_disabled_for_invalid_email() {
        loadLoginContent()

        val emailField = composeTestRule.onNodeWithText("Email")
        emailField.performTextClearance()
        emailField.performTextInput("user-at-example.com")

        val passwordField = composeTestRule.onNodeWithText("Senha")
        passwordField.performTextClearance()
        passwordField.performTextInput("senha1234")

        composeTestRule.onNodeWithText("Entrar").assertIsNotEnabled()
    }

    fun loginButton_stays_disabled_for_invalid_password() {
        loadLoginContent()

        val emailField = composeTestRule.onNodeWithText("Email")
        emailField.performTextClearance()
        emailField.performTextInput("user@example.com")

        val passwordField = composeTestRule.onNodeWithText("Senha")
        passwordField.performTextClearance()
        passwordField.performTextInput("senha123")

        composeTestRule.onNodeWithText("Entrar").assertIsNotEnabled()
    }

}

