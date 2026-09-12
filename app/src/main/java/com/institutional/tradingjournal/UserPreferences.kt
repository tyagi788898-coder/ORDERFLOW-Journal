package com.institutional.tradingjournal

import android.content.Context

object UserPreferences {
    private const val PREFS_NAME = "orderflow_prefs"
    private const val KEY_USER_EMAIL = "user_email"
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_LOGGED_IN = "is_logged_in"

    fun saveUser(context: Context, email: String, username: String) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
            .putString(KEY_USER_EMAIL, email)
            .putString(KEY_USER_NAME, username)
            .putBoolean(KEY_LOGGED_IN, true)
            .apply()
    }

    fun getUserEmail(context: Context) = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getString(KEY_USER_EMAIL, "") ?: ""
    fun getUserName(context: Context) = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getString(KEY_USER_NAME, "Trader") ?: "Trader"
    fun setUserName(context: Context, name: String) = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit().putString(KEY_USER_NAME, name).apply()
    fun isLoggedIn(context: Context) = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getBoolean(KEY_LOGGED_IN, false)
}
