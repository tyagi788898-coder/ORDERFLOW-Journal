package com.institutional.tradingjournal.data

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest

object UserDataStore {

    private val firebaseAuth: FirebaseAuth
        get() = FirebaseAuth.getInstance()

    /**
     * Email + Password Signup
     */
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
                    onResult(false, "Account created but user session was not found.")
                    return@addOnCompleteListener
                }

                // Save username/display name in Firebase profile
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(
                        if (cleanUsername.isNotEmpty()) {
                            cleanUsername
                        } else {
                            cleanEmail.substringBefore("@")
                        }
                    )
                    .build()

                user.updateProfile(profileUpdates)
                    .addOnCompleteListener {
                        onResult(true, null)
                    }
            }
    }

    /**
     * Email + Password Login
     */
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

    /**
     * Google Login using Firebase Authentication
     */
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
                        task.exception?.localizedMessage ?: "Google login failed."
                    )
                }
            }
    }

    /**
     * Check whether the currently logged-in Firebase user
     * matches the supplied email.
     */
    fun userExists(
        context: Context,
        email: String
    ): Boolean {
        val currentUser = firebaseAuth.currentUser
        val cleanEmail = email.trim().lowercase()

        return currentUser?.email?.trim()?.lowercase() == cleanEmail
    }

    /**
     * Get username/display name of the current Firebase user.
     */
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

        return cleanEmail.substringBefore("@")
            .ifEmpty { "Trader" }
    }

    /**
     * Send Firebase password-reset email.
     *
     * Firebase handles the actual password reset securely.
     */
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

    /**
     * Get currently logged-in Firebase user's email.
     */
    fun getCurrentSession(context: Context): String? {
        return firebaseAuth.currentUser?.email
    }

    /**
     * Kept for compatibility with existing app code.
     * Firebase itself manages the authenticated session.
     */
    fun setSession(
        context: Context,
        email: String
    ) {
        // Firebase Authentication manages the session automatically.
    }

    /**
     * Logout from Firebase.
     */
    fun clearSession(context: Context) {
        firebaseAuth.signOut()
    }

    /**
     * Returns the currently authenticated Firebase user.
     */
    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }
}
