package com.oussama.todolistapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.oussama.todolistapp.models.User

class PreferenceManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREF_NAME, Context.MODE_PRIVATE
    )

    fun saveUser(user: User) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_USER_ID, user.id)
        editor.putString(KEY_EMAIL, user.email)
        editor.putString(KEY_USERNAME, user.username)
        editor.apply()
    }

    fun getUser(): User? {
        val id = sharedPreferences.getString(KEY_USER_ID, null) ?: return null
        val email = sharedPreferences.getString(KEY_EMAIL, "") ?: ""
        val username = sharedPreferences.getString(KEY_USERNAME, "") ?: ""
        return User(id, email, username, "")
    }

    fun clearUser() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    fun isUserLoggedIn(): Boolean {
        return sharedPreferences.contains(KEY_USER_ID)
    }

    companion object {
        private const val PREF_NAME = "TodoListPrefs"
        private const val KEY_USER_ID = "userId"
        private const val KEY_EMAIL = "email"
        private const val KEY_USERNAME = "username"
    }
}