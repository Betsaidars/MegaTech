package com.example.megatech

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.megatech.Model.UserModel

class SessionManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("megaTech2_prefs", Context.MODE_PRIVATE)
    private val editor = prefs.edit() // Usa la misma instancia de SharedPreferences para el editor

    private val USER_ID_KEY = "USER_ID"
    private val USER_NAME_KEY = "USER_NAME"
    private val USER_EMAIL_KEY = "USER_EMAIL"

    fun saveUserModel(user: UserModel) {
        editor.putString(USER_ID_KEY, user.id).apply()
        editor.putString(USER_NAME_KEY, user.username).apply()
        editor.putString(USER_EMAIL_KEY, user.email).apply()
        Log.d("SessionManager", "User model saved: ${user.id}, ${user.username}, ${user.email}")
        Log.d("SessionManager", "Prefs content: ${prefs.all}")
    }

    fun getUserModel(): UserModel? {
        val userId = prefs.getString(USER_ID_KEY, null) // Devuelve null si no existe
        val userName = prefs.getString(USER_NAME_KEY, null)
        val userEmail = prefs.getString(USER_EMAIL_KEY, null)

        if (userId != null && userName != null && userEmail != null) {
            val user = UserModel(userId, userName, userEmail, "")
            Log.d("SessionManager", "User model retrieved: ${user.id}, ${user.username}, ${user.email}")
            Log.d("SessionManager", "Prefs content: ${prefs.all}")
            return user
        } else {
            Log.d("SessionManager", "User model not found")
            return null
        }
    }

    fun clearSession() {
        editor.clear().apply()
        Log.d("SessionManager", "Session cleared")
    }

    fun logout() {
        editor.clear().apply() // Usa la misma instancia para limpiar
    }
}