package com.institutional.tradingjournal.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.institutional.tradingjournal.data.UserDataStore
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToWelcome: () -> Unit,
    onNavigateToDashboard: () -> Unit
) {
    val context = LocalContext.current
    val alphaAnim = remember { Animatable(0f) }

    val iconResId = remember(context) {
        val mipmapId = context.resources.getIdentifier("ic_launcher", "mipmap", context.packageName)
        if (mipmapId != 0) mipmapId else context.resources.getIdentifier("ic_launcher", "drawable", context.packageName)
    }

    LaunchedEffect(Unit) {
        alphaAnim.animateTo(1f, animationSpec = tween(700))
        delay(600)
        val activeSession = UserDataStore.getCurrentSession(context)
        if (!activeSession.isNullOrBlank()) {
            onNavigateToDashboard()
        } else {
            onNavigateToWelcome()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090A0F)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.alpha(alphaAnim.value)
        ) {
            if (iconResId != 0) {
                Image(
                    painter = painterResource(id = iconResId),
                    contentDescription = "Official App Logo",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(20.dp))
                )
            } else {
                Text("📈", fontSize = 54.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Orderflow Journal Book",
                color = Color(0xFFFFC107),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Institutional Edge & Execution Analysis",
                color = Color.Gray,
                fontSize = 13.sp
            )
        }
    }
}
