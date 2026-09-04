package com.institutional.tradingjournal.data

import android.content.Context
import android.content.SharedPreferences

object UserDataStore {
    private const val PREF_NAME = "orderflow_auth_secure_store"
    private const val KEY_LOGGED_IN = "is_user_logged_in"
    private const val KEY_CURRENT_USER = "current_session_user"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun registerUser(context: Context, email: String, pass: String, username: String): Boolean {
        val cleanEmail = email.trim().lowercase()
        val prefs = getPrefs(context)
        
        // Save user credentials
        prefs.edit()
            .putString("pwd_$cleanEmail", pass.trim())
            .putString("name_$cleanEmail", username.trim())
            .putBoolean(KEY_LOGGED_IN, true)
            .putString(KEY_CURRENT_USER, cleanEmail)
            .apply()
        return true
    }

    fun authenticate(context: Context, email: String, pass: String): Boolean {
        val cleanEmail = email.trim().lowercase()
        val prefs = getPrefs(context)
        val savedPass = prefs.getString("pwd_$cleanEmail", null) ?: return false
        val matched = savedPass == pass.trim()
        if (matched) {
            prefs.edit()
                .putBoolean(KEY_LOGGED_IN, true)
                .putString(KEY_CURRENT_USER, cleanEmail)
                .apply()
        }
        return matched
    }

    fun userExists(context: Context, email: String): Boolean {
        val cleanEmail = email.trim().lowercase()
        return getPrefs(context).contains("pwd_$cleanEmail")
    }

    fun resetPassword(context: Context, email: String, newPass: String): Boolean {
        val cleanEmail = email.trim().lowercase()
        val prefs = getPrefs(context)
        if (!userExists(context, cleanEmail)) return false
        prefs.edit().putString("pwd_$cleanEmail", newPass.trim()).apply()
        return true
    }

    fun getUsername(context: Context, email: String): String {
        val cleanEmail = email.trim().lowercase()
        return getPrefs(context).getString("name_$cleanEmail", "Trader") ?: "Trader"
    }

    fun getCurrentSession(context: Context): String? {
        val prefs = getPrefs(context)
        val isLoggedIn = prefs.getBoolean(KEY_LOGGED_IN, false)
        return if (isLoggedIn) prefs.getString(KEY_CURRENT_USER, null) else null
    }

    fun logout(context: Context) {
        getPrefs(context).edit().putBoolean(KEY_LOGGED_IN, false).remove(KEY_CURRENT_USER).apply()
    }
}
