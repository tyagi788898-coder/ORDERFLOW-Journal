package com.institutional.tradingjournal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.institutional.tradingjournal.ui.navigation.NavGraph
import com.institutional.tradingjournal.ui.theme.OrderflowTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OrderflowTheme {
                NavGraph()
            }
        }
    }
}
