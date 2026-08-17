package com.institutional.tradingjournal.ui.screens

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.institutional.tradingjournal.GoogleAuthHelper
import com.institutional.tradingjournal.UserPreferences

const val GOOGLE_WEB_CLIENT_ID = "618729179730-7l7pb3joupbmc4n734u9nn5qt6o1ngjk.apps.googleusercontent.com"

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onNavigateToSignup: () -> Unit) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val userEmail = account.email ?: ""
            UserPreferences.saveUser(context, userEmail, "Trader_${(1000..9999).random()}")
            onLoginSuccess()
        } catch (e: Exception) {
            Toast.makeText(context, "Account not found. Please Signup.", Toast.LENGTH_SHORT).show()
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF090A0F)).padding(24.dp)) {
        Text("Orderflow Journal Book", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(32.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, modifier = Modifier.fillMaxWidth())
        
        Button(onClick = {
            if (email.isNotBlank()) {
                UserPreferences.saveUser(context, email, "Trader_User")
                onLoginSuccess()
            } else {
                Toast.makeText(context, "Please enter email", Toast.LENGTH_SHORT).show()
            }
        }, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) { Text("Log in") }

        Spacer(modifier = Modifier.height(16.dp))
        
        // Google Login
        Surface(color = Color(0xFF12141C), modifier = Modifier.fillMaxWidth().height(50.dp).clickable {
            googleSignInLauncher.launch(GoogleAuthHelper.getSignInIntent(context, GOOGLE_WEB_CLIENT_ID))
        }) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                Text("G", fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Continue with Google", color = Color.White)
            }
        }
        Text(text = "By using Orderflow Journal, you agree to our Terms.", color = Color.Gray, fontSize = 10.sp, modifier = Modifier.padding(top = 20.dp))
    }
}
