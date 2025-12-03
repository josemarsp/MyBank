package com.example.mybank.feature.transfer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.mybank.core.viewmodel.TransferUiState
import com.example.mybank.core.viewmodel.TransferViewModel
import com.example.mybank.ui.components.BankButton
import com.example.mybank.ui.components.BankTopBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun TransferScreen(
    onComplete: () -> Unit
) {
    val viewModel: TransferViewModel = koinViewModel()
    val state by viewModel.uiState.collectAsState()

    var destination by rememberSaveable { mutableStateOf("4321-9") }
    var amount by rememberSaveable { mutableStateOf("250.00") }
    var message by rememberSaveable { mutableStateOf("Pagamento aluguel") }

    val isValid = amount.toDoubleOrNull() ?: 0.0 > 0.0 && destination.isNotBlank()

    Scaffold(
        topBar = {
            BankTopBar(
                title = "Transferência",
                actions = {
                    IconButton(onClick = onComplete) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = destination,
                onValueChange = { destination = it },
                label = { Text("Conta destino") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Valor (R$)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text("Descrição") },
                modifier = Modifier.fillMaxWidth()
            )
            BankButton(
                label = if (state.isLoading) "Enviando..." else "Enviar transferência",
                onClick = {
                    val amountValue = amount.toDoubleOrNull() ?: 0.0
                    viewModel.submitTransfer(destination, amountValue, message)
                },
                enabled = isValid && !state.isLoading,
                modifier = Modifier.fillMaxWidth()
            )
            state.error?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }
            state.success?.let {
                Text(
                    text = "Transferência confirmada (ID $it)",
                    color = MaterialTheme.colorScheme.primary
                )
                BankButton(
                    label = "Voltar ao dashboard",
                    onClick = onComplete,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

