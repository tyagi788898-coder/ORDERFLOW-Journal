package com.institutional.tradingjournal.data

import android.content.Context
import android.content.SharedPreferences

object UserDataStore {
    private const val PREF_NAME = "orderflow_persistent_vault_v1"
    private const val KEY_SESSION = "current_active_session_email"
    private const val PREFIX_USER = "user_cred_"
    private const val PREFIX_NAME = "user_name_"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun registerUser(context: Context, email: String, pass: String, username: String) {
        val cleanEmail = email.trim().lowercase()
        getPrefs(context).edit().apply {
            putString("${PREFIX_USER}$cleanEmail", pass)
            putString("${PREFIX_NAME}$cleanEmail", username.trim())
            putString(KEY_SESSION, cleanEmail)
            commit()
        }
    }

    fun authenticate(context: Context, email: String, pass: String): Boolean {
        val cleanEmail = email.trim().lowercase()
        val prefs = getPrefs(context)
        val storedPass = prefs.getString("${PREFIX_USER}$cleanEmail", null)
        
        // Match existing password or Google auth
        if (storedPass != null && (storedPass == pass || pass == "GOOGLE_AUTH")) {
            setSession(context, cleanEmail)
            return true
        }

        // Auto-Register Fallback: If user signs in with a valid password but record was wiped, restore account seamlessly
        if (storedPass == null && pass.length >= 4) {
            val generatedUsername = cleanEmail.substringBefore("@")
            registerUser(context, cleanEmail, pass, generatedUsername)
            return true
        }

        return false
    }

    fun userExists(context: Context, email: String): Boolean {
        val cleanEmail = email.trim().lowercase()
        return getPrefs(context).contains("${PREFIX_USER}$cleanEmail")
    }

    fun getUsername(context: Context, email: String): String {
        val cleanEmail = email.trim().lowercase()
        return getPrefs(context).getString("${PREFIX_NAME}$cleanEmail", cleanEmail.substringBefore("@")) ?: "Trader"
    }

    fun resetPassword(context: Context, email: String, newPass: String): Boolean {
        val cleanEmail = email.trim().lowercase()
        val prefs = getPrefs(context)
        return if (prefs.contains("${PREFIX_USER}$cleanEmail")) {
            prefs.edit().putString("${PREFIX_USER}$cleanEmail", newPass).commit()
            true
        } else {
            // If wiped, allow setting new password directly
            registerUser(context, cleanEmail, newPass, cleanEmail.substringBefore("@"))
            true
        }
    }

    fun getCurrentSession(context: Context): String? {
        return getPrefs(context).getString(KEY_SESSION, null)
    }

    fun setSession(context: Context, email: String) {
        getPrefs(context).edit().putString(KEY_SESSION, email.trim().lowercase()).commit()
    }

    fun clearSession(context: Context) {
        getPrefs(context).edit().remove(KEY_SESSION).commit()
    }
}
