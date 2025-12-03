package com.example.mybank.feature.transactions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mybank.core.model.TransactionResponse
import com.example.mybank.core.model.TransactionType
import com.example.mybank.core.viewmodel.TransactionsUiState
import com.example.mybank.core.viewmodel.TransactionsViewModel
import com.example.mybank.ui.components.BankTopBar
import com.example.mybank.ui.components.TransactionCard
import com.example.mybank.ui.theme.WhiteLabelBrand
import com.example.mybank.ui.theme.WhiteLabelTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun TransactionsScreen(onBack: () -> Unit) {
    val viewModel: TransactionsViewModel = koinViewModel()
    val state by viewModel.uiState.collectAsState()
    TransactionsScreenContent(
        state = state,
        onBack = onBack
    )
}

@Composable
fun TransactionsScreenContent(
    state: TransactionsUiState,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            BankTopBar(
                title = "Transações",
                actions = {
                    IconButton(onClick = onBack) {
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
                .padding(16.dp)
        ) {
            if (state.transactions.isEmpty() && state.error == null) {
                Text("Carregando...")
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(state.transactions) { transaction ->
                        TransactionCard(transaction = transaction)
                    }
                }
            }
            state.error?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

private val previewTransactions = listOf(
    TransactionResponse("tx-101", "2025-11-29", "Pix salário", 1800.0, TransactionType.CREDIT, "PIX"),
    TransactionResponse("tx-102", "2025-11-28", "Padaria", -42.50, TransactionType.DEBIT, "Alimentação"),
    TransactionResponse("tx-103", "2025-11-27", "Posto", -120.20, TransactionType.DEBIT, "Mobilidade")
)

@Preview(showBackground = true)
@Composable
fun TransactionsScreenPreview() {
    WhiteLabelTheme(brand = WhiteLabelBrand.ClientA) {
        TransactionsScreenContent(
            state = TransactionsUiState(
                transactions = previewTransactions
            ),
            onBack = {}
        )
    }
}

