package com.institutional.tradingjournal.data

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest

object UserDataStore {

    private val firebaseAuth: FirebaseAuth
        get() = FirebaseAuth.getInstance()

    fun registerUser(
        context: Context,
        email: String,
        pass: String,
        username: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        val cleanEmail = email.trim().lowercase()
        val cleanUsername = username.trim()

        firebaseAuth
            .createUserWithEmailAndPassword(cleanEmail, pass)
            .addOnCompleteListener { task ->

                if (!task.isSuccessful) {
                    onResult(
                        false,
                        task.exception?.localizedMessage ?: "Signup failed."
                    )
                    return@addOnCompleteListener
                }

                val user = firebaseAuth.currentUser

                if (user == null) {
                    onResult(
                        false,
                        "Account created but user session was not found."
                    )
                    return@addOnCompleteListener
                }

                val finalUsername =
                    if (cleanUsername.isNotEmpty()) {
                        cleanUsername
                    } else {
                        cleanEmail.substringBefore("@")
                    }

                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(finalUsername)
                    .build()

                user.updateProfile(profileUpdates)
                    .addOnCompleteListener {
                        onResult(true, null)
                    }
            }
    }

    fun authenticate(
        context: Context,
        email: String,
        pass: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        val cleanEmail = email.trim().lowercase()

        firebaseAuth
            .signInWithEmailAndPassword(cleanEmail, pass)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(
                        false,
                        task.exception?.localizedMessage ?: "Login failed."
                    )
                }
            }
    }

    fun authenticateWithGoogle(
        context: Context,
        idToken: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        firebaseAuth
            .signInWithCredential(credential)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(
                        false,
                        task.exception?.localizedMessage
                            ?: "Google login failed."
                    )
                }
            }
    }

    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    fun getCurrentSession(context: Context): String? {
        return firebaseAuth.currentUser?.email
    }

    fun getUsername(
        context: Context,
        email: String
    ): String {
        val user = firebaseAuth.currentUser

        if (user != null) {
            val displayName = user.displayName?.trim()

            if (!displayName.isNullOrEmpty()) {
                return displayName
            }

            val userEmail = user.email?.trim()

            if (!userEmail.isNullOrEmpty()) {
                return userEmail.substringBefore("@")
            }
        }

        val cleanEmail = email.trim().lowercase()

        return cleanEmail
            .substringBefore("@")
            .ifEmpty { "Trader" }
    }

    fun updateUsername(
        context: Context,
        username: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        val user = firebaseAuth.currentUser

        if (user == null) {
            onResult(false, "No logged-in user.")
            return
        }

        val cleanUsername = username.trim()

        if (cleanUsername.isEmpty()) {
            onResult(false, "Username cannot be empty.")
            return
        }

        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(cleanUsername)
            .build()

        user.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(
                        false,
                        task.exception?.localizedMessage
                            ?: "Unable to update username."
                    )
                }
            }
    }

    fun resetPassword(
        context: Context,
        email: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        val cleanEmail = email.trim().lowercase()

        if (cleanEmail.isEmpty()) {
            onResult(false, "Please enter your email address.")
            return
        }

        firebaseAuth
            .sendPasswordResetEmail(cleanEmail)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(
                        false,
                        task.exception?.localizedMessage
                            ?: "Unable to send password reset email."
                    )
                }
            }
    }

    fun userExists(
        context: Context,
        email: String
    ): Boolean {
        val currentUser = firebaseAuth.currentUser
        val cleanEmail = email.trim().lowercase()

        return currentUser?.email
            ?.trim()
            ?.lowercase() == cleanEmail
    }

    fun setSession(
        context: Context,
        email: String
    ) {
        // Firebase Authentication automatically manages the session.
    }

    fun clearSession(
        context: Context
    ) {
        firebaseAuth.signOut()
    }
}
