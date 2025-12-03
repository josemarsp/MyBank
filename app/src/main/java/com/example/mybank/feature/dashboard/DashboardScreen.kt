package com.example.mybank.feature.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mybank.core.model.BalanceResponse
import com.example.mybank.core.model.TransactionResponse
import com.example.mybank.core.model.TransactionType
import com.example.mybank.core.viewmodel.DashboardViewModel
import com.example.mybank.core.viewmodel.DashboardUiState
import com.example.mybank.ui.components.BankTopBar
import com.example.mybank.ui.components.TransactionCard
import com.example.mybank.ui.theme.WhiteLabelBrand
import com.example.mybank.ui.theme.WhiteLabelTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun DashboardScreen(
    onNavigateTransactions: () -> Unit,
    onNavigateTransfer: () -> Unit,
    onInitiateCapture: () -> Unit
) {
    val viewModel: DashboardViewModel = koinViewModel()
    val state by viewModel.uiState.collectAsState()
    DashboardScreenContent(
        state = state,
        onNavigateTransactions = onNavigateTransactions,
        onNavigateTransfer = onNavigateTransfer,
        onInitiateCapture = onInitiateCapture
    )
}

@Composable
fun DashboardScreenContent(
    state: DashboardUiState,
    onNavigateTransactions: () -> Unit,
    onNavigateTransfer: () -> Unit,
    onInitiateCapture: () -> Unit
) {
    Scaffold(
        topBar = {
            BankTopBar(
                title = "MyBank",
                subtitle = "Saldo e movimentos"
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            BalanceCard(uiState = state)
            Spacer(modifier = Modifier.height(16.dp))
            ActionRow(
                onTransactions = onNavigateTransactions,
                onTransfer = onNavigateTransfer,
                onCapture = onInitiateCapture
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Transações recentes",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(DashboardScreenTestTags.LoadingIndicator),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .testTag(DashboardScreenTestTags.TransactionList),
                    contentPadding = PaddingValues(bottom = 48.dp)
                ) {
                    items(state.transactions) { transaction ->
                        TransactionCard(transaction = transaction)
                    }
                }
            }
            state.error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.testTag(DashboardScreenTestTags.ErrorText)
                )
            }
        }
    }
}

@Composable
private fun BalanceCard(uiState: DashboardUiState) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Saldo disponível", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = uiState.balance?.let { "%s %.2f".format( it.currency, it.available) }
                    ?: "Carregando...",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = uiState.balance?.lastUpdated ?: "Sincronizando...",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun ActionRow(
    onTransactions: () -> Unit,
    onTransfer: () -> Unit,
    onCapture: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = onTransactions,
            modifier = Modifier
                .weight(1f)
                .testTag(DashboardScreenTestTags.TransactionsButton)
        ) {
            Icon(Icons.Default.List, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Extrato")
        }
        Spacer(
            modifier = Modifier
                .width(16.dp)
        )
        Button(
            onClick = onTransfer,
            modifier = Modifier
                .weight(1f)
                .testTag(DashboardScreenTestTags.TransferButton)
        ) {
            Icon(Icons.Default.SwapHoriz, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Transferir")
        }
//        IconButton(
//            onClick = onCapture,
//            modifier = Modifier.testTag(DashboardScreenTestTags.CaptureButton)
//        ) {
//            Icon(Icons.Default.CameraAlt, contentDescription = "Capturar documento")
//        }
    }
}

object DashboardScreenTestTags {
    const val LoadingIndicator = "DashboardLoadingIndicator"
    const val TransactionList = "DashboardTransactionList"
    const val ErrorText = "DashboardErrorText"
    const val TransactionsButton = "DashboardTransactionsButton"
    const val TransferButton = "DashboardTransferButton"
    const val CaptureButton = "DashboardCaptureButton"
}

private val previewTransactions = listOf(
    TransactionResponse("tx-001", "2025-11-30", "Pix recebido", 1200.0, TransactionType.CREDIT, "PIX"),
    TransactionResponse("tx-002", "2025-11-29", "Mercado", -220.35, TransactionType.DEBIT, "Compras"),
    TransactionResponse("tx-003", "2025-11-28", "Restaurante", -95.50, TransactionType.DEBIT, "Alimentação")
)

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    WhiteLabelTheme(brand = WhiteLabelBrand.ClientA) {
        DashboardScreenContent(
            state = DashboardUiState(
                balance = BalanceResponse(11211.5, "R$", "2025-11-29"),
                transactions = previewTransactions
            ),
            onNavigateTransactions = {},
            onNavigateTransfer = {},
            onInitiateCapture = {}
        )
    }
}
