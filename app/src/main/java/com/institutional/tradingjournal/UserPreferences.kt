package com.institutional.tradingjournal

import android.content.Context

object UserPreferences {
    private const val PREFS_NAME = "trade_journal_user_prefs"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"
    private const val KEY_USER_EMAIL = "user_email"
    private const val KEY_USER_NAME = "user_name"

    fun setLoggedIn(context: Context, isLoggedIn: Boolean, email: String = "") {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
            .putString(KEY_USER_EMAIL, email)
            .apply()
    }

    fun isLoggedIn(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun getUserEmail(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_USER_EMAIL, "") ?: ""
    }

    fun setUserName(context: Context, name: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_USER_NAME, name).apply()
    }

    fun getUserName(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val saved = prefs.getString(KEY_USER_NAME, "")
        if (!saved.isNullConfigurationsOrBlank()) return saved
        val randomNum = (1000..9999).random()
        val defaultName = "Trader_$randomNum"
        setUserName(context, defaultName)
        return defaultName
    }

    private fun String?.isNullConfigurationsOrBlank(): Boolean = this.isNullOrBlank()
}
