package com.institutional.tradingjournal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.institutional.tradingjournal.ui.navigation.NavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Force window background programmatically to eliminate any white flash
        window.decorView.setBackgroundColor(android.graphics.Color.parseColor("#090A0F"))
        
        setContent {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF090A0F)) // Immediate Dark Layer
            ) {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}
