package com.institutional.tradingjournal.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SplashScreen(
    onLoadingComplete: () -> Unit
) {
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(key1 = true) {
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000)
        )
        kotlinx.coroutines.delay(1200)
        onLoadingComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090A0F)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.alpha(alpha.value)
        ) {
            Text(
                text = "📊 INSTITUTIONAL",
                color = Color(0xFFFFC107),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "ORDERFLOW JOURNAL PRO",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            // Custom Developer Credits
            Text(
                text = "Made with Suraj & Neeraj Tyagi",
                color = Color(0xFF00E676),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            LinearProgressIndicator(
                progress = alpha.value,
                modifier = Modifier
                    .width(180.dp)
                    .height(4.dp),
                color = Color(0xFFFFC107),
                trackColor = Color(0xFF1A1D28)
            )
        }
    }
}

