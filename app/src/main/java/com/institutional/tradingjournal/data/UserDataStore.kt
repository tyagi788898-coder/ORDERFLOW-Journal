package com.institutional.tradingjournal.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class UserAccount(
    val email: String,
    val passwordHash: String,
    val username: String,
    val createdAt: Long = System.currentTimeMillis()
)

object UserDataStore {
    private const val PREFS_NAME = "orderflow_users_db"
    private const val KEY_USERS = "registered_users_list"
    private const val KEY_SESSION = "current_session_email"
    private val gson = Gson()

    fun getAllUsers(context: Context): MutableMap<String, UserAccount> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_USERS, null) ?: return mutableMapOf()
        val type = object : TypeToken<MutableMap<String, UserAccount>>() {}.type
        return try {
            gson.fromJson(json, type) ?: mutableMapOf()
        } catch (e: Exception) {
            mutableMapOf()
        }
    }

    private fun saveUsers(context: Context, users: Map<String, UserAccount>) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_USERS, gson.toJson(users)).apply()
    }

    fun registerUser(context: Context, email: String, password: String, username: String): Boolean {
        val cleanEmail = email.trim().lowercase()
        val users = getAllUsers(context)
        if (users.containsKey(cleanEmail)) {
            return false // User already exists
        }
        users[cleanEmail] = UserAccount(
            email = cleanEmail,
            passwordHash = password.trim(),
            username = username.trim()
        )
        saveUsers(context, users)
        setCurrentSession(context, cleanEmail)
        return true
    }

    fun authenticate(context: Context, email: String, password: String): Boolean {
        val cleanEmail = email.trim().lowercase()
        val users = getAllUsers(context)
        val account = users[cleanEmail] ?: return false
        return account.passwordHash == password.trim()
    }

    fun userExists(context: Context, email: String): Boolean {
        return getAllUsers(context).containsKey(email.trim().lowercase())
    }

    fun resetPassword(context: Context, email: String, newPassword: String): Boolean {
        val cleanEmail = email.trim().lowercase()
        val users = getAllUsers(context)
        val account = users[cleanEmail] ?: return false
        users[cleanEmail] = account.copy(passwordHash = newPassword.trim())
        saveUsers(context, users)
        return true
    }

    fun setCurrentSession(context: Context, email: String?) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_SESSION, email?.trim()?.lowercase() ?: "").apply()
    }

    fun getCurrentSession(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val session = prefs.getString(KEY_SESSION, "")
        return if (session.isNullOrBlank()) null else session
    }

    fun clearSession(context: Context) {
        setCurrentSession(context, null)
    }
}
