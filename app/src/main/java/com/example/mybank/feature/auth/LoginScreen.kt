package com.example.mybank.feature.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mybank.core.viewmodel.AuthUiState
import com.example.mybank.core.viewmodel.AuthViewModel
import com.example.mybank.ui.components.BankButton
import com.example.mybank.ui.components.BankTopBar
import com.example.mybank.ui.theme.WhiteLabelBrand
import com.example.mybank.ui.theme.WhiteLabelTheme
import org.koin.androidx.compose.koinViewModel

private val emailPattern =
    Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z0-9]{2,6}$")

fun isLoginInputValid(email: String, password: String): Boolean {
    return email.isNotBlank() &&
        password.isNotBlank() &&
        password.length >= 8 &&
        emailPattern.matches(email)
}

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit
) {
    val viewModel: AuthViewModel = koinViewModel()
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state) {
        if (state is AuthUiState.Success) {
            onLoginSuccess()
        }
    }

    LoginContent(
        uiState = state,
        onLogin = { email, password ->
            viewModel.login(email, password)
        }
    )
}

@Composable
fun LoginContent(
    uiState: AuthUiState,
    onLogin: (email: String, password: String) -> Unit
) {
    var email by rememberSaveable { mutableStateOf("cliente@mybank.com") }
    var password by rememberSaveable { mutableStateOf("senha12") }
    var emailClick by remember { mutableStateOf(true) }
    var passwordClick by remember { mutableStateOf(true) }
    val isLoginEnabled = isLoginInputValid(email, password)

    Scaffold(
        topBar = {
            BankTopBar(title = "Bem-vindo ao MyBank", subtitle = "Entre com sua conta")
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = email,
                singleLine = true,
                onValueChange = { email = it },
               leadingIcon = { Icon(Icons.Default.MailOutline, contentDescription = null) },
               keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
               modifier = Modifier
                   .fillMaxWidth()
                   .onFocusChanged { state ->
                       if (state.isFocused && emailClick) {
                           email = ""
                           emailClick = false
                       }
                   },
               label = { Text("Email") }
           )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = password,
                singleLine = true,
                onValueChange = { password = it },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { state ->
                        if (state.isFocused && passwordClick) {
                            password = ""
                            passwordClick = false
                        }
                    },
                label = { Text("Senha") }
            )
            Spacer(modifier = Modifier.height(24.dp))
            BankButton(
                label = if (uiState is AuthUiState.Loading) "Validando..." else "Entrar",
                onClick = { onLogin(email, password) },
                enabled = isLoginEnabled && uiState !is AuthUiState.Loading,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            if (uiState is AuthUiState.Error) {
                Text(
                    text = uiState.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    WhiteLabelTheme(brand = WhiteLabelBrand.ClientA) {
        LoginContent(uiState = AuthUiState.Idle, onLogin = { _, _ -> })
    }
}

