package com.institutional.tradingjournal.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.institutional.tradingjournal.UserPreferences

// 1. Welcome Screen (Screenshot 3 Matching)
@Composable
fun WelcomeScreen(
    onNavigateToSignup: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val bgColor = Color(0xFF090A0F)
    val cardBg = Color(0xFF12141C)
    val primaryBlue = Color(0xFF1976D2)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // App Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = Color(0xFF1E2638),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("📈", fontSize = 18.sp)
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Trading Journal Book",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Center Illustration Circle
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                color = Color(0xFFFFE082),
                shape = CircleShape,
                modifier = Modifier.size(180.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("📓", fontSize = 80.sp)
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Trading Journal Book",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Analyse trades in a better way",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }

        // Action Buttons
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onNavigateToSignup,
                colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Create New Account",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("➔", color = Color.White, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onNavigateToLogin,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = cardBg),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = "I already have an account",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "By using TradeLog, you agree to our Terms of Service\nand Privacy Policy.",
                color = Color.Gray,
                fontSize = 11.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

// 2. Login Screen (Screenshot 1 Matching)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToSignup: () -> Unit
) {
    val context = LocalContext.current
    val bgColor = Color(0xFF090A0F)
    val inputBg = Color(0xFF12141C)
    val primaryBlue = Color(0xFF1976D2)

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(24.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Top Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                color = Color(0xFF1E2638),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("📈", fontSize = 18.sp)
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "TradeJournal",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Welcome Back",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Track and analyze your trades",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 4.dp, bottom = 32.dp)
        )

        // Email Field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Email", color = Color.Gray) },
            leadingIcon = { Text("✉️", fontSize = 16.sp) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = primaryBlue,
                unfocusedBorderColor = Color(0xFF2A2E3D),
                focusedContainerColor = inputBg,
                unfocusedContainerColor = inputBg
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Password Field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Password", color = Color.Gray) },
            leadingIcon = { Text("🔒", fontSize = 16.sp) },
            trailingIcon = {
                Text(
                    text = if (passwordVisible) "👁️" else "🙈",
                    modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                )
            },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = primaryBlue,
                unfocusedBorderColor = Color(0xFF2A2E3D),
                focusedContainerColor = inputBg,
                unfocusedContainerColor = inputBg
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 24.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = "Forgot Password?",
                color = primaryBlue,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    Toast.makeText(context, "Password reset link sent to your email", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // Login Button
        Button(
            onClick = {
                if (email.isNotBlank() && password.isNotBlank()) {
                    UserPreferences.setLoggedIn(context, true, email)
                    Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()
                    onLoginSuccess()
                } else {
                    Toast.makeText(context, "Please enter valid Email and Password", Toast.LENGTH_SHORT).show()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text(
                text = "Log in with Email",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // OR Divider
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFF2A2E3D))
            Text("  OR  ", color = Color.Gray, fontSize = 12.sp)
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFF2A2E3D))
        }

        // Social Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                color = inputBg,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .clickable {
                        UserPreferences.setLoggedIn(context, true, "google_user@trade.com")
                        onLoginSuccess()
                    }
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🔴 ", fontSize = 14.sp)
                    Text("Google", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }

            Surface(
                color = inputBg,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .clickable {
                        UserPreferences.setLoggedIn(context, true, "phone_user@trade.com")
                        onLoginSuccess()
                    }
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("📱 ", fontSize = 14.sp)
                    Text("Phone", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Create a new account",
                color = primaryBlue,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onNavigateToSignup() }
            )
        }
    }
}

// 3. Signup Screen (Screenshot 2 Matching)
@Composable
fun SignupScreen(
    onSignupSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val context = LocalContext.current
    val bgColor = Color(0xFF090A0F)
    val inputBg = Color(0xFF12141C)
    val primaryBlue = Color(0xFF1976D2)

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(24.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "← Back",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 8.dp, bottom = 24.dp)
                .clickable { onNavigateToLogin() }
        )

        Text(
            text = "Create New Account",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Start your trading journey today.",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 4.dp, bottom = 32.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Email", color = Color.Gray) },
            leadingIcon = { Text("✉️", fontSize = 16.sp) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = primaryBlue,
                unfocusedBorderColor = Color(0xFF2A2E3D),
                focusedContainerColor = inputBg,
                unfocusedContainerColor = inputBg
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Password", color = Color.Gray) },
            leadingIcon = { Text("🔒", fontSize = 16.sp) },
            trailingIcon = {
                Text(
                    text = if (passwordVisible) "👁️" else "🙈",
                    modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                )
            },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = primaryBlue,
                unfocusedBorderColor = Color(0xFF2A2E3D),
                focusedContainerColor = inputBg,
                unfocusedContainerColor = inputBg
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )

        Button(
            onClick = {
                if (email.isNotBlank() && password.isNotBlank()) {
                    UserPreferences.setLoggedIn(context, true, email)
                    Toast.makeText(context, "Account Created Successfully!", Toast.LENGTH_SHORT).show()
                    onSignupSuccess()
                } else {
                    Toast.makeText(context, "Please enter valid Email and Password", Toast.LENGTH_SHORT).show()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text(
                text = "Create Account",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFF2A2E3D))
            Text("  OR  ", color = Color.Gray, fontSize = 12.sp)
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFF2A2E3D))
        }

        Surface(
            color = inputBg,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clickable {
                    UserPreferences.setLoggedIn(context, true, "google_signup@trade.com")
                    onSignupSuccess()
                }
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("🔴 ", fontSize = 14.sp)
                Text("Continue with Google", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Already have an account? Log in",
                color = primaryBlue,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onNavigateToLogin() }
            )
        }
    }
}
