package com.example.mybank

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mybank.ui.navigation.BankNavHost
import com.example.mybank.ui.theme.WhiteLabelBrand
import com.example.mybank.ui.theme.WhiteLabelTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WhiteLabelTheme(brand = WhiteLabelBrand.ClientA) {
                BankNavHost()
            }
        }
    }
}