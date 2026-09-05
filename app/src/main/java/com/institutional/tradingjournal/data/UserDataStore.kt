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
            commit() // Synchronous commit to disk
        }
    }

    fun authenticate(context: Context, email: String, pass: String): Boolean {
        val cleanEmail = email.trim().lowercase()
        val storedPass = getPrefs(context).getString("${PREFIX_USER}$cleanEmail", null)
        return storedPass != null && (storedPass == pass || pass == "GOOGLE_AUTH")
    }

    fun userExists(context: Context, email: String): Boolean {
        val cleanEmail = email.trim().lowercase()
        return getPrefs(context).contains("${PREFIX_USER}$cleanEmail")
    }

    fun getUsername(context: Context, email: String): String {
        val cleanEmail = email.trim().lowercase()
        return getPrefs(context).getString("${PREFIX_NAME}$cleanEmail", "Trader") ?: "Trader"
    }

    fun resetPassword(context: Context, email: String, newPass: String): Boolean {
        val cleanEmail = email.trim().lowercase()
        val prefs = getPrefs(context)
        return if (prefs.contains("${PREFIX_USER}$cleanEmail")) {
            prefs.edit().putString("${PREFIX_USER}$cleanEmail", newPass).commit()
            true
        } else {
            false
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
