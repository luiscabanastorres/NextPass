package com.nextpass.nextiati.data.preferences

import android.content.SharedPreferences


class PreferencesImpl(
    private val preferences: SharedPreferences
): Preferences {
    override fun saveToken(token: String?) {
        preferences.edit().putString(TOKEN, token).apply()
    }

    override fun getToken() = preferences.getString(TOKEN, null)

    companion object {
        private const val TOKEN = "Token"
    }
}